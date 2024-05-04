import {authenticationStorage} from 'public/scripts/utils.js';

document.getElementById('login-form').addEventListener('submit', async function (event) {
    event.preventDefault();
    const username = document.getElementById('username').value;
    const password = document.getElementById('password').value;
    await login(username, password)
});

async function login(username, password) {
    const response = await fetch('/api/auth', {
        method: 'POST', body: JSON.stringify({username, password})
    });

    if (!response.ok) {
        const errorMessage = await response.text();
        document.getElementById('form-error').innerHTML = `${errorMessage}`;
        return;
    }

    authenticationStorage.save(username, password);
    window.location.href = '/';
}
