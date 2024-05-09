if (authenticationStorage.get() === null) {
    window.location.href = '/register';
}

const lastMessageId = messageService.getLastMessageId();
console.log(lastMessageId)

document.getElementById('logout').addEventListener('click', () => userService.logout());