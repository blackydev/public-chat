const delay = ms => new Promise(res => setTimeout(res, ms));

class MessageService {
    static #ENDPOINT = "/api/messages";
    static #MAX_BATCH_SIZE = 5;
    #olderGotMessageId = null;
    #newerGotMessageId = null;

    constructor() {
        this.#getLastMessageId().then(lastMessageId => {
            this.#olderGotMessageId = lastMessageId + 1;
            this.#newerGotMessageId = lastMessageId;
        })
    }

    async create(content) {
        await fetch(MessageService.#ENDPOINT, {
            method: 'POST', headers: {"Authorization": authenticationStorage.get()}, body: JSON.stringify({content})
        });
    }

    async getNewerMessages() {
        if (!this.#isInitialized()) {
            await delay(200);
            return await this.getNewerMessages()
        }
        const minId = this.#olderGotMessageId;
        const maxId = this.#newerGotMessageId + MessageService.#MAX_BATCH_SIZE;
        const response = await fetch(MessageService.#ENDPOINT, {
            method: 'GET',
            headers: {"Authorization": authenticationStorage.get()},
            body: {olderGotMessageId: minId, newerGotMessageId: maxId}
        });
        const messages = await response.json();
        this.#newerGotMessageId += messages.length;
        return messages.sort((m1, m2) => m1.id - m2.id);
    }

    async getOlderMessages() {
        if (this.#olderGotMessageId < 0) {
            return [];
        }
        if (!this.#isInitialized()) {
            await delay(200);
            return await this.getOlderMessages()
        }
        const minId = this.#olderGotMessageId - MessageService.#MAX_BATCH_SIZE;
        const maxId = this.#newerGotMessageId;
        const endpointAndQuery = `${MessageService.#ENDPOINT}?minId=${minId}&maxId=${maxId}`;
        const response = await fetch(endpointAndQuery, {
            method: 'GET',
            headers: {"Authorization": authenticationStorage.get()},
        });
        this.#olderGotMessageId = minId;
        return await response.json();
    }

    async #getLastMessageId() {
        const response = await fetch(`${MessageService.#ENDPOINT}/last/id`, {
            method: 'GET', headers: {"Authorization": authenticationStorage.get()}
        });
        if (response.status === 401) {
            userService.logout();
        }
        return (await response.json()).sort((m1, m2) => m1.id - m2.id);
    }

    #isInitialized() {
        return this.#olderGotMessageId !== null && this.#newerGotMessageId !== null;
    }
}

const messageService = new MessageService();
