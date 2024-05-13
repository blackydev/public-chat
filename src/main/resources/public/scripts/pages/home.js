addAdminNavBars();
loadMoreMessages();
setInterval(updateMessages, 2000);
document.getElementById("message-more").addEventListener('click', loadMoreMessages)

function addAdminNavBars() {
    if (authenticationStorage.isAdmin())
        htmlBoardService.addToNavBar("Permissions", "/permissions/admin");
}

async function loadMoreMessages() {
    const messages = await messageService.getOlderMessages()
    messages.reverse().forEach(htmlBoardService.addMessage);
}

async function updateMessages() {
    const messages = await messageService.getNewerMessages()
    messages.forEach(msg => htmlBoardService.addMessage(msg, true));
}

// MESSAGE FORM - SEND
document.getElementById('message').addEventListener('submit', sendMessage);

async function sendMessage(event) {
    event.preventDefault();
    const content = document.getElementById('message-content');
    if (content.value !== '') {
        await messageService.create(content.value);
        content.value = '';
    }
    await updateMessages();
}

// NAVIGATION
document.getElementById('logout').addEventListener('click', userService.logout);