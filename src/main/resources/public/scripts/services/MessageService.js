class MessageService {
    static #ENDPOINT = "/api/messages";
    static #MAX_BATCH_SIZE = 5;
    #olderGotMessageId = null;
    #newerGotMessageId = null;

    constructor() {
        this.#getLastMessageId().then(lastMessageId => {
            console.log()
            this.#olderGotMessageId = lastMessageId + 1;
            this.#newerGotMessageId = lastMessageId;
        })
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
        console.log(response.body)
        return response.body
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
        console.log(response.body)
        return response.body
    }

    async #getLastMessageId() {
        const response = await fetch(`${MessageService.#ENDPOINT}/last/id`, {
            method: 'GET', headers: {"Authorization": authenticationStorage.get()}
        });
        if (response.status === 401) {
            userService.logout();
        }
        return response.body;
    }

    #isInitialized() {
        return this.#olderGotMessageId !== null && this.#newerGotMessageId !== null;
    }
}

const messageService = new MessageService();
