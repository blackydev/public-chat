class UserService {
    async register(username, password) {
        await this.#sendRequest('/api/users', username, password)
    }

    async login(username, password) {
        await this.#sendRequest('/api/auth', username, password)
    }

    async updateCurrent(username, password) {
        await this.#sendRequest('/api/users/me', username, password, 'PUT');
    }

    logout() {
        authenticationStorage.clear();
        window.location.href = '/login';
    }

    async addAdminPermissions(userId) {
        await this.#sendEditPermissionsRequest(userId, 'POST');
    }

    async removeAdminPermissions(userId) {
        await this.#sendEditPermissionsRequest(userId, 'DELETE');
    }

    async #sendRequest(endpoint, username, password, method = "POST") {
        const response = await fetch(endpoint, {
            body: JSON.stringify({username, password}),
            headers: {"Authorization": authenticationStorage.getAuthentication()},
            method,
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

    async #sendEditPermissionsRequest(userId, method = "POST") {
        const response = await fetch(`/api/users/${userId}/permissions/admin`, {
            method, headers: {"Authorization": authenticationStorage.getAuthentication()}
        });
        if (!response.ok) {
            const errorMessage = await response.text();
            document.getElementById('error').innerHTML = `${errorMessage}`;
        }
    }
}

const userService = new UserService();