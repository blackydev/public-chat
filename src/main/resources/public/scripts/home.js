import {authenticationStorage} from 'public/scripts/utils.js';

if (authenticationStorage.get() === null) {
    window.location.href = '/register';
}
