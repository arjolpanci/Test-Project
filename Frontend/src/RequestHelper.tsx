const API_URL = 'http://localhost:8080';

export default async function makeRequest(path: string, method: string, body: any, shouldPassAuth: boolean) {
    let token = null;
    if(shouldPassAuth) {
        token = sessionStorage.getItem("app-16acfb35-token");
    }
    let headers = {
        'Content-Type': 'application/json',
        ...(shouldPassAuth && {"Authorization": `Bearer ${token}`})
    };

    const response = await fetch(API_URL + path, {
        method: method,
        cache: 'no-cache',
        headers: headers,
        body: JSON.stringify(body),
    });
    return response.json();
}