class AuthenticationStorage {
    static #KEY = "Authentication";

    get() {
        return `Basic ${localStorage.getItem(AuthenticationStorage.#KEY)}`;
    }

    save(username, password) {
        localStorage.setItem(AuthenticationStorage.#KEY, btoa(`${username}:${password}`));
    }

    clear() {
        localStorage.removeItem(AuthenticationStorage.#KEY);
    }
}

const authenticationStorage = new AuthenticationStorage();