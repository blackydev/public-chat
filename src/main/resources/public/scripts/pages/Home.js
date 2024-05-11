if (authenticationStorage.get() === null) {
    window.location.href = '/register';
}

messageService.getOlderMessages().then(messages => {
    messages.forEach(message => addHtmlMessage(message.author.username, message.content, message.timestamp));
})

document.getElementById('message').addEventListener('submit', async function (event) {
    event.preventDefault();
    const content = document.getElementById('message-content');
    if (content.value !== '') {
        await messageService.create(content.value);
        content.value = '';
    }
});


function addHtmlMessage(authorName, content, datetime) {
    console.log("Add html message");
    document.getElementById('board').innerHtml = `
        <div class="message-box">
            <div class="author">${authorName}</div>
            <div class="content">${content}</div>
            <div class="footer flex-center-space-between flex-row-reverse">
                <div class="date">${datetime}</div>
                <button class="remove">remove</button>
            </div>
        </div>
    `;
}

document.getElementById('logout').addEventListener('click', () => userService.logout());