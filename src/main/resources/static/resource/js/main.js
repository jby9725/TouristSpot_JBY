$(document).ready(function () {
    const cityMapping = {
        "서울": "Seoul",
        "경기도": "Gyeonggi-do",
        "부산": "Busan",
        "경상남도": "Gyeongsangnam-do",
        "인천": "Incheon",
        "경상북도": "Gyeongsangbuk-do",
        "대구": "Daegu",
        "전라남도": "Jeollanam-do",
        "전북": "Jeollabuk-do",
        "충청남도": "Chungcheongnam-do",
        "충청북도": "Chungcheongbuk-do",
        "강원도": "Gangwon-do",
        "광주": "Gwangju",
        "대전": "Daejeon",
        "울산": "Ulsan",
        "제주도": "Jeju-do",
        "세종": "Sejong"
    };

    // 시/도 데이터 가져오기
    $('#city-select').on('focus', function () {
        if ($('#city-select option').length === 1) { // 이미 데이터가 로드되었는지 확인
            $.ajax({
                url: '/cities',
                method: 'GET',
                success: function (data) {
                    // select 박스에 시/도 추가
                    for (let city of data) {
                        $('#city-select').append(
                            `<option value="${city}">${city}</option>`
                        );
                    }
                },
                error: function () {
                    alert('시/도 데이터를 불러오는 데 실패했습니다.');
                }
            });
        }
    });

    // 검색 버튼 클릭 이벤트
    $('#search-button').on('click', function () {
        const selectedCity = $('#city-select').val();
        if (!selectedCity) {
            alert('시/도를 선택해주세요.');
            return;
        }

        // 선택한 시/도로 검색 작업 시작
        $('#weather_result').html(`<p class="text-lg font-bold">날씨 정보를 불러오는 중...</p>`);
        $('#tourism_result').html(`<p class="text-lg font-bold">관광지 정보를 불러오는 중...</p>`);

        // 날씨 정보 가져오기 (영어 이름으로 변환 후 요청)
        const englishCityName = cityMapping[selectedCity];
        if (!englishCityName) {
            $('#weather_result').html(`<p class="text-lg text-red-500">날씨 정보를 요청할 수 없습니다.</p>`);
            return;
        }

        $.ajax({
            url: `/weather?city=${englishCityName}`, // 날씨 API 엔드포인트
            method: 'GET',
            success: function (data) {
                console.log("날씨 응답 데이터:", data);

                const weatherHtml = `
                    <h3 class="text-xl font-bold">${selectedCity}의 날씨 정보</h3>
                    <p>현재 온도: ${data.main?.temp || '정보 없음'}℃</p>
                    <p>체감 온도: ${data.main?.feels_like || '정보 없음'}℃</p>
                    <p>최저/최고 온도: ${data.main?.temp_min || '정보 없음'}℃ / ${data.main?.temp_max || '정보 없음'}℃</p>
                    <p>날씨 상태: ${data.weather?.[0]?.description || '정보 없음'}</p>
                    <p>풍속: ${data.wind?.speed || '정보 없음'} m/s</p>
                `;
                $('#weather_result').html(weatherHtml);
            },
            error: function (jqXHR) {
                console.error("날씨 API 호출 실패:", jqXHR);
                $('#weather_result').html(`<p class="text-lg text-red-500">날씨 정보를 불러오는 데 실패했습니다. 오류 코드: ${jqXHR.status}</p>`);
            }
        });


        // 관광지 정보 가져오기
        $.ajax({
            url: `/tourism/search?keyword=${selectedCity}`,
            method: 'GET',
            success: function (data) {
                // console.log("관광지 응답 데이터:", data);
                // console.log("Raw data:", data);
                // console.log("Type of data:", typeof data);

                let parsedData;
                try {
                    // JSON 문자열을 객체로 변환
                    parsedData = JSON.parse(data);
                } catch (e) {
                    console.error("JSON 파싱 오류:", e);
                    $('#tourism_result').html('<p class="text-lg text-red-500">데이터를 처리할 수 없습니다.</p>');
                    return;
                }

                // JSON 구조 확인 및 데이터 추출
                let items = [];
                if (parsedData.response && parsedData.response.body && parsedData.response.body.items && parsedData.response.body.items.item) {
                    items = parsedData.response.body.items.item;
                } else {
                    $('#tourism_result').html('<p class="text-lg text-red-500">관광지 정보가 없습니다.</p>');
                    return;
                }

                // 데이터가 배열이 아닌 단일 객체로 반환되는 경우 처리
                if (!Array.isArray(items)) {
                    items = [items];
                }

                // 데이터 표시
                let html = `<h3 class="text-xl font-bold">${selectedCity}의 관광지 정보</h3><ul>`;
                items.forEach(item => {
                    html += `
                        <li class="mt-2">
                            <strong>${item.title || '제목 없음'}</strong><br>
                            <span>주소: ${item.addr1 || '주소 없음'} ${item.addr2 || ''}</span><br>
                            <span>전화번호: ${item.tel || '전화번호 없음'}</span><br>
                            <span>좌표: (${item.mapx || 'N/A'}, ${item.mapy || 'N/A'})</span>
                        </li>
                    `;
                });
                html += '</ul>';
                $('#tourism_result').html(html);
            },
            error: function () {
                $('#tourism_result').html(`<p class="text-lg text-red-500">관광지 정보를 불러오는 데 실패했습니다.</p>`);
            }
        });


        // 관광지 정보 가져오기
        // $.ajax({
        //     url: `/tourism/search?keyword=${selectedCity}`,
        //     method: 'GET',
        //     success: function (data) {
        //         console.log("관광지 응답 데이터:", data);
        //
        //         // 데이터를 보기 좋게 포매팅
        //         const formattedData = JSON.stringify(data, null, 2); // JSON 포매팅
        //         $('#tourism_result').html(`
        //             <h3 class="text-xl font-bold">${selectedCity}의 관광지 정보</h3>
        //             <pre style="white-space: pre-wrap; background-color: #f9f9f9; padding: 10px; border: 1px solid #ddd;">${formattedData}</pre>
        //         `);
        //     },
        //     error: function () {
        //         $('#tourism_result').html(`<p class="text-lg text-red-500">관광지 정보를 불러오는 데 실패했습니다.</p>`);
        //     }
        // });
    });
});
