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
        const response = await fetch(MessageService.#ENDPOINT, {
            method: 'POST', headers: {"Authorization": authenticationStorage.get()}, body: JSON.stringify({content})
        });
        return await response.json();
    }

    async getNewerMessages() {
        if (!this.#isInitialized()) {
            return [];
        }
        const olderGotMessageId = this.#olderGotMessageId;
        const newerGotMessageId = this.#newerGotMessageId + MessageService.#MAX_BATCH_SIZE;
        const response = await fetch(MessageService.#ENDPOINT, {
            method: 'GET',
            headers: {"Authorization": authenticationStorage.get()},
            body: {olderGotMessageId, newerGotMessageId}
        });
        this.#newerGotMessageId = newerGotMessageId; // todo
        return await response.json();
    }

    async getOlderMessages() {
        if (!this.#isInitialized()) {
            return [];
        }
        const olderGotMessageId = this.#olderGotMessageId - MessageService.#MAX_BATCH_SIZE;
        const newerGotMessageId = this.#newerGotMessageId;
        const response = await fetch(MessageService.#ENDPOINT, {
            method: 'GET',
            headers: {"Authorization": authenticationStorage.get()},
            body: {olderGotMessageId, newerGotMessageId}
        });
        this.#olderGotMessageId = olderGotMessageId;
        return await response.json();
    }

    async #getLastMessageId() {
        const response = await fetch(`${MessageService.#ENDPOINT}/last/id`, {
            method: 'GET', headers: {"Authorization": authenticationStorage.get()}
        });
        if (response.status === 401) {
            userService.logout();
        }
        return await response.json();
    }

    #isInitialized() {
        return this.#olderGotMessageId !== null && this.#newerGotMessageId !== null;
    }
}

const messageService = new MessageService();
