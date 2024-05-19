const delay = ms => new Promise(res => setTimeout(res, ms));

class MessageService {
    static #ENDPOINT = "/api/messages";
    static #MAX_BATCH_SIZE = 10;
    #minId = null;
    #maxId = null;

    constructor() {
        this.#getLastMessageId().then(lastMessageId => {
            this.#minId = lastMessageId + 1;
            this.#maxId = lastMessageId + 1;
        })
    }

    async create(content) {
        await fetch(MessageService.#ENDPOINT, {
            method: 'POST', headers: {"Authorization": authenticationStorage.getAuthentication()}, body: JSON.stringify({content})
        });
    }

    async getNewerMessages() {
        if (!this.#isInitialized()) {
            await delay(200);
            return await this.getNewerMessages()
        }
        const from = this.#maxId;
        const to = this.#maxId + MessageService.#MAX_BATCH_SIZE;
        const messages = await this.#getMessages(from, to);

        this.#maxId += messages.length;
        return messages;
    }

    async getOlderMessages() {
        if (this.isLastMessageGot()) {
            return [];
        }
        if (!this.#isInitialized()) {
            await delay(200);
            return await this.getOlderMessages()
        }
        const from = this.#minId - MessageService.#MAX_BATCH_SIZE;
        const to = this.#minId - 1;
        const messages = await this.#getMessages(from, to);

        this.#minId = from;
        return messages
    }

    isLastMessageGot() {
        return this.#minId != null && this.#minId <= 0;
    }

    async #getMessages(minId, maxId) {
        const endpointAndQuery = `${MessageService.#ENDPOINT}?minId=${minId}&maxId=${maxId}`;
        const response = await fetch(endpointAndQuery, {
            method: 'GET',
            headers: {"Authorization": authenticationStorage.getAuthentication()},
        });
        if (response.status === 401) {
            userService.logout();
            return null;
        }
        return (await response.json()).sort((m1, m2) => m2.id - m1.id);
    }

    async #getLastMessageId() {
        const response = await fetch(`${MessageService.#ENDPOINT}/last/id`, {
            method: 'GET', headers: {"Authorization": authenticationStorage.getAuthentication()}
        });
        if (response.status === 401) {
            userService.logout();
        }
        return await response.json();
    }

    async remove(messageId) {
        const endpointAndQuery = `${MessageService.#ENDPOINT}/${messageId}`;
        const response = await fetch(endpointAndQuery, {
            method: 'DELETE',
            headers: {"Authorization": authenticationStorage.getAuthentication()},
        });
    }

    #isInitialized() {
        return this.#minId !== null && this.#maxId !== null;
    }
}

const messageService = new MessageService();
