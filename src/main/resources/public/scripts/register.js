document.getElementById('registration-form').addEventListener('submit', function (event) {
    event.preventDefault();

    const username = document.getElementById('username').value;
    const password = document.getElementById('password').value;

    fetch('/api/users', {
        method: 'POST', body: {username, password}
    })
        .then(res => {
            if (!res.ok) {
                return res.text().then(errorMessage => {
                    document.getElementById('form-error').innerHTML = `${errorMessage}`;
                    throw new Error(`Request failed with status ${res.status}`);
                });
            }
            localStorage.setItem("accessToken", btoa(`${username}:${password}`));
            window.location.href = '/';
        })
        .catch(error => {
            console.error('Error:', error);
        });
});