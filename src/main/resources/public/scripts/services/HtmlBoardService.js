class HtmlBoardService {
    addMessage(message, addAtBeginning = false) {
        const date = new Date(message.timestamp);

        const removeButton = authenticationStorage.isAdmin()
            ? '<button class="remove">remove</button>'
            : '';

        const htmlMessage = `
        <div class="message-box mb-25px">
            <div class="author">${message.author.username}</div>
            <div class="content">${message.content}</div>
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

    addToNavBar(content, url) {
        const nav = document.getElementsByTagName('nav')[0];
        nav.innerHtml += `<a href="${url}">${content}</a>`;
    }
}

const htmlBoardService = new HtmlBoardService();