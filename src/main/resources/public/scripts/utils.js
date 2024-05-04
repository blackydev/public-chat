class AuthenticationStorage {
    #key = "Authentication";

    get() {
        return localStorage.getItem(this.#key);
    }

    save(username, password) {
        localStorage.setItem(this.#key, btoa(`Basic ${username}:${password}`));
    }

    clear() {
        localStorage.removeItem(this.#key);
    }
}

const authenticationStorage = new AuthenticationStorage();

export {authenticationStorage};