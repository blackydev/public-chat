if (authenticationStorage.getAuthentication() === null) {
    window.location.href = '/register';
}

loadMoreMessages();
setInterval(updateMessages, 500);
document.getElementById("message-more").addEventListener('click', () => loadMoreMessages())

async function loadMoreMessages() {
    const messages = await messageService.getOlderMessages()
    messages.reverse().forEach(msg => addHtmlMessageToBoard(msg));
}

async function updateMessages() {
    const messages = await messageService.getNewerMessages()
    messages.forEach(msg => addHtmlMessageToBoard(msg, true));
}

function addHtmlMessageToBoard(msg, addAtBeginning = false) {
    const date = new Date(msg.timestamp);

    let removeButton = '';
    if (authenticationStorage.isAdmin())
        removeButton = '<button class="remove">remove</button>'

    const htmlMessage = `
        <div class="message-box mb-25px">
            <div class="author">${msg.author.username}</div>
            <div class="content">${msg.content}</div>
            <div class="footer flex-center-space-between flex-row-reverse">
                <div class="date">${date.getDate()}.${date.getMonth()}.${date.getFullYear()} ${date.getHours()}:${date.getMinutes()}</div>
                ${removeButton}
            </div>
        </div>
    `;
    const board = document.getElementById('board');
    if (addAtBeginning) {
        board.innerHTML = htmlMessage + board.innerHTML
    } else {
        board.innerHTML += htmlMessage;
    }
}

// MESSAGE FORM - SEND
document.getElementById('message').addEventListener('submit', () => sendMessage());

async function sendMessage(event) {
    event.preventDefault();
    const content = document.getElementById('message-content');
    if (content.value !== '') {
        await messageService.create(content.value);
        content.value = '';
    }
}

// NAVIGATION
document.getElementById('logout').addEventListener('click', () => userService.logout());