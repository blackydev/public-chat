class UserService {
    async register(username, password) {
        await this.#sendRequest('/api/users', username, password)
    }

    async login(username, password) {
        await this.#sendRequest('/api/auth', username, password)
    }

    logout() {
        authenticationStorage.clear();
        window.location.href = '/login';
    }

    async #sendRequest(endpoint, username, password) {
        const response = await fetch(endpoint, {
            method: 'POST', body: JSON.stringify({username, password})
        });

        if (!response.ok) {
            const errorMessage = await response.text();
            document.getElementById('error').innerHTML = `${errorMessage}`;
            return;
        }

        authenticationStorage.save(username, password);
        window.location.href = '/';
    }
}

const userService = new UserService();