import {authenticationStorage} from 'public/scripts/utils.js';

document.getElementById('registration-form').addEventListener('submit', async function (event) {
    event.preventDefault();
    const username = document.getElementById('username').value;
    const password = document.getElementById('password').value;
    await register(username, password)
});

async function register(username, password) {
    const response = await fetch('/api/users', {
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