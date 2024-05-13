class UserService {
    async register(username, password) {
        await this.#sendRequest('/api/users', username, password)
    }

    async login(username, password) {
        await this.#sendRequest('/api/auth', username, password)
    }

    async update(username, password) {
        await this.#sendRequest('/api/users/me', username, password, 'PUT');
    }

    logout() {
        authenticationStorage.clear();
        window.location.href = '/login';
    }

    async #sendRequest(endpoint, username, password, method = "POST") {
        const response = await fetch(endpoint, {
            method, body: JSON.stringify({username, password})
        });

        if (!response.ok) {
            const errorMessage = await response.text();
            document.getElementById('error').innerHTML = `${errorMessage}`;
            return;
        }
        const resBody = await response.json();

        authenticationStorage.save(username, password, resBody.isAdmin);
        window.location.href = '/';
    }
}

const userService = new UserService();