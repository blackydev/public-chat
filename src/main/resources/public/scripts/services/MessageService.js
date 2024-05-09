class MessageService {
    static #ENDPOINT = "/api/messages";

    async getLastMessageId() {
        const response = await fetch(`${MessageService.#ENDPOINT}/last/id`, {
            method: 'GET', headers: {"Authorization": authenticationStorage.get()}
        });
        if (response.status === 401) {
            userService.logout();
        }
        return response.body;
    }

    async getMessages(minId, maxId) {
        const response = await fetch(MessageService.#ENDPOINT, {
            method: 'GET', headers: {"Authorization": authenticationStorage.get()}, body: {minId, maxId}
        });
        const messages = response.body;
        console.log(messages)
        return response.body
    }
}

const messageService = new MessageService();