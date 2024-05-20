if (!authenticationStorage.isAdmin()) {
    window.location.href = '/';
}

document.getElementById('admin-set').addEventListener('click', async function (event) {
    event.preventDefault();
    const username = document.getElementById('username').value;
    await userService.addAdminPermissions(username);
});

document.getElementById('admin-remove').addEventListener('click', async function (event) {
    event.preventDefault();
    const username = document.getElementById('username').value;
    await userService.removeAdminPermissions(username);
});