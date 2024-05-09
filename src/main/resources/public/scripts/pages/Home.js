if (authenticationStorage.get() === null) {
    window.location.href = '/register';
}

document.getElementById('logout').addEventListener('click', () => userService.logout());