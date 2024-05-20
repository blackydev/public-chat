class AuthenticationStorage {
    static #AUTHENTICATION = "authentication";
    static #IS_ADMIN = "isAdmin";

    getAuthentication() {
        const authentication = localStorage.getItem(AuthenticationStorage.#AUTHENTICATION);
        if (authentication !== null) {
            return `Basic ${localStorage.getItem(AuthenticationStorage.#AUTHENTICATION)}`;
        }
        return null;
    }

    isAdmin() {
        return JSON.parse(localStorage.getItem(AuthenticationStorage.#IS_ADMIN));
    }

    save(username, password, isAdmin) {
        localStorage.setItem(AuthenticationStorage.#AUTHENTICATION, btoa(`${username}:${password}`));
        localStorage.setItem(AuthenticationStorage.#IS_ADMIN, isAdmin);
    }

    clear() {
        localStorage.removeItem(AuthenticationStorage.#AUTHENTICATION);
        localStorage.removeItem(AuthenticationStorage.#IS_ADMIN);
    }
}

const authenticationStorage = new AuthenticationStorage();