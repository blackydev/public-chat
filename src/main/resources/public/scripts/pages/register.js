authenticationStorage.clear();
document.getElementById('registration-form').addEventListener('submit', async function (event) {
    event.preventDefault();
    const username = document.getElementById('username').value;
    const password = document.getElementById('password').value;
    await userService.register(username, password)
});
