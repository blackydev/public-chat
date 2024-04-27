console.log(localStorage.getItem("accessToken"));
if(localStorage.getItem("accessToken") === null) {
    window.location.href = '/register';
}
