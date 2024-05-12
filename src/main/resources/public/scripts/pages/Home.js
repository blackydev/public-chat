if (authenticationStorage.get() === null) {
    window.location.href = '/register';
}

printLastMessages();
setInterval(() => printNewMessages(), 500);

async function printNewMessages() {
    const messages = await messageService.getNewerMessages()
    messages.forEach(msg => addHtmlMessageAtEnd(msg, true));
}

async function printLastMessages() {
    const messages = await messageService.getOlderMessages()
    messages.reverse().forEach(msg => addHtmlMessageAtEnd(msg));
}

document.getElementById('message').addEventListener('submit', async function (event) {
    event.preventDefault();
    const content = document.getElementById('message-content');
    if (content.value !== '') {
        await messageService.create(content.value);
        content.value = '';
    }
});


function addHtmlMessageAtEnd(msg, isNewMessage = false) {
    const date = new Date(msg.timestamp);
    console.log("Add html message");
    const htmlMessage = `
        <div class="message-box">
            <div class="author">${msg.author.username}</div>
            <div class="content">${msg.content}</div>
            <div class="footer flex-center-space-between flex-row-reverse">
                <div class="date">${date.getDay()}</div>
                <button class="remove">remove</button>
            </div>
        </div>
    `;
    const board = document.getElementById('board');
    if (isMessageNew) {
        board.innerHTML = htmlMessage + board.innerHTML
    } else {
        board.innerHTML += htmlMessage;
    }

}

document.getElementById('logout').addEventListener('click', () => userService.logout());