if (authenticationStorage.get() === null) {
    window.location.href = '/register';
}

messageService.getOlderMessages().then(messages => {
    messages.forEach(message => addHtmlMessage(message.author, message.content, message.timestamp));
})

document.getElementById('message').addEventListener('submit', async function (event) {
    event.preventDefault();
    const content = document.getElementById('message-content').value;
    if (content !== '') {
        await messageService.create(content); // todo [Error] Unhandled Promise Rejection: SyntaxError: The string did not match the expected pattern.
    }
});

function addHtmlMessage(author, content, date) {
    document.getElementById('board').innerHtml = `
        <div class="message-box">
            <div class="author">${author}</div>
            <div class="content">${content}</div>
            <div class="footer flex-center-space-between flex-row-reverse">
                <div class="date">${date}</div>
                <button class="remove">remove</button>
            </div>
        </div>
    `;
}

document.getElementById('logout').addEventListener('click', () => userService.logout());