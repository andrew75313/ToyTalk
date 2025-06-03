$(document).ready(function () {
    // 로그인 시 응답 헤더에서 토큰 저장
    $(document).ajaxSuccess(function (event, xhr, settings) {
        const accessToken = xhr.getResponseHeader('Authorization');
        const refreshToken = xhr.getResponseHeader('refreshToken');

        if (accessToken) {
            localStorage.setItem('accessToken', accessToken);
        }
        if (refreshToken) {
            localStorage.setItem('refreshToken', refreshToken);
        }
    });

    // 모든 요청에 accessToken 자동 첨부
    $.ajaxSetup({
        beforeSend: function (xhr, settings) {
            const excludedUrls = [
                '/api/login',
                '/api/users/signup',
                '/',                // 랜딩 페이지
                '/api/logout'       // 필요 시 로그아웃도 제외 가능
            ];
            const isExcluded = excludedUrls.some(url => settings.url.startsWith(url));

            if (!isExcluded) {
                const token = localStorage.getItem('accessToken');
                if (token) {
                    xhr.setRequestHeader('Authorization', token);
                }
            }
        }
    });
});
