document.getElementById('user-update-form').addEventListener('submit', async function (event) {
    event.preventDefault();
    const username = document.getElementById('username').value;
    const password = document.getElementById('password').value;
    await userService.updateCurrent(username, password);
});