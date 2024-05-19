addAdminNavBars();
loadMoreMessages();
setInterval(updateMessages, 2000);
const loadMoreMessageHtmlButton = document.getElementById("message-more");
loadMoreMessageHtmlButton.addEventListener('click', loadMoreMessages);
removeUnusedMessageMoreButton();

function removeUnusedMessageMoreButton() {
    if (messageService.isLastMessageGot()) {
        loadMoreMessageHtmlButton.remove();
    } else {
        setTimeout(removeUnusedMessageMoreButton, 1000);
    }
}

function addAdminNavBars() {
    if (authenticationStorage.isAdmin()) {
        htmlBoardService.showAdminNav();
    }
}

async function loadMoreMessages() {
    const messages = await messageService.getOlderMessages()
    messages.forEach(msg => htmlBoardService.addMessage(msg, false));
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