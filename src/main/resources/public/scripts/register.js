document.getElementById('registration-form').addEventListener('submit', function (event) {
    event.preventDefault();

    const username = document.getElementById('username').value;
    const password = document.getElementById('password').value;

    fetch('/api/users', {
        method: 'POST', body: {username, password}
    })
        .then(response => {
            if (!response.ok) {
                return response.text().then(errorMessage => {
                    document.getElementById('form-error').innerHTML = `${errorMessage}`;
                    throw new Error(`Request failed with status ${response.status}`);
                });
            }
            localStorage.setItem("accessToken", btoa(`${username}:${password}`));
            window.location.href = '/';
        })
        .catch(error => {
            console.error('Error:', error);
        });
});