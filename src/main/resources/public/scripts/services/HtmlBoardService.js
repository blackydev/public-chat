class HtmlBoardService {
    addMessage(message, addAtBeginning = false) {
        const date = new Date(message.timestamp);
        const isAdmin = authenticationStorage.isAdmin();
        let removeButton = '';
        if (isAdmin) {
            removeButton = `
            <button data-message-id="${message.id}" class="remove" onclick="htmlBoardService.removeMessage(${message.id})"> 
                remove 
            </button>
        `; // some problems with button.addListener('click', ...);
        }

        const htmlMessage = `
        <div data-message-id="${message.id}" class="message-box mb-25px">
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

    showAdminNav() {
        const navChildren = document.querySelector('nav').children;
        for (let i = 0; i < navChildren.length; i++) {
            navChildren[i].classList.remove('hidden');
        }
    }

    removeMessage(messageId) {
        document.querySelector(`.message-box[data-message-id="${messageId}"]`).remove();
        messageService.remove(messageId);
    }
}

const htmlBoardService = new HtmlBoardService();