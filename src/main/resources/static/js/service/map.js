

// 전역에 var변수는 전역객체의 속성으로 등록됨
var mymap = {
    map: null,        // 지도객체
    whereAmI: {      // 내위치정보
        lat: '',
        lng: ''
    }
};

// 키워드 검색 완료 시 호출되는 콜백함수 입니다
const markers = [];
const infowindows = [];

// 마커 제거
function removeMaker(marker) {
    marker.setMap(null);
}

// 마커 제거
function removeMakers(markers) {
    markers.forEach(marker => marker.setMap(null));
}

// 검색 결과 목록
function makePlaceList(data, pagination, map, markers, infowindows) {
    const searchResultEle = document.createElement('div');          //<div></div>
    searchResultEle.classList.add('search_result');                 //<div class='search_result'></div>

    const placeListEle = document.createElement('div');             //<div></div>
    placeListEle.classList.add('place_list');                       //<div class='place_list'></div>

    const pagingEle = document.createElement('div');                //<div></div>
    pagingEle.classList.add('paging');                              //<div class='paging'></div>

    searchResultEle.appendChild(placeListEle);                      //<div class='search_result'><div class='place_list'></div></div>
    searchResultEle.appendChild(pagingEle)                          //<div class='search_result'><div class='paging'></div></div>

    const place_list_html = data.map((place, idx) => {
        return `<div><span data-idx="${idx}">${place.place_name}</span></div>`
    }
    ).join('');                                                     //<div><span data-idx="0">장소명1</span></div><div><span data-idx="1">장소명2</span></div>...

    placeListEle.innerHTML = place_list_html;

    //목록이벤트 등록
    const lists = [...placeListEle.querySelectorAll('span')];       // join('')에서 없앴던 구분자를 사실상 다시 스프레드 연산자를통해 구분자를 가지게 (고차함수 사용을 하기 위함)
    lists.forEach((item, idx) => {
        item.addEventListener('mouseover', e => {
            infowindows[idx].open(map, markers[idx]);               //어떤지도의 어떤마커의 인포윈도우를 열건지
        }, false);
        item.addEventListener('mouseout', e => {
            infowindows[idx].close(map, markers[idx]);               //mouseout시 인포윈도우 닫아야
        }, false);
    }
    );

    //페이징 이벤트 등록
    let paging_html = '';
    for (let i = pagination.first; i <= pagination.last; i++) {
        paging_html += `<span>${i}</span>`;
    }
    pagingEle.innerHTML = paging_html;

    const pages = [...pagingEle.querySelectorAll('span')];
    pages.forEach(page =>
        page.addEventListener('click', e => {
            // console.log(e.target.textContent)
            const pageNum = e.target.textContent;
            pagination.gotoPage(pageNum);
        }, false)
    );

    //document.body.appendChild(searchResultEle);
    return searchResultEle;
}



function placesSearchCB(data, status, pagination) {
    console.dir(data);
    console.log(status)
    console.dir(pagination);

    if (status === kakao.maps.services.Status.OK) {

        // 기존 마커 정보 제거
        removeMakers(markers);

        // 마커 배열 요소 제거
        if (markers.length > 0) {
            // case1)
            // markers.length = 0;
            // case2)
            markers.splice(0, markers.length);
        }

        // 정보창 배열 요소 제거
        if (infowindows.length > 0) {
            infowindows.splice(0, infowindows.length);
        }

        data.forEach(place => {
            //마커 생성
            markers.push(makeMarker(mymap.map, place.y, place.x));

            //정보창 생성
            infowindows.push(new naver.maps.InfoWindow({ content: place.place_name }));
        })

        // 마커 mouseover 시 인포창 표시하기
        markers.forEach((marker, idx) => kakao.maps.event.addListener(marker, 'mouseover', e => {
            infowindows[idx].open(mymap.map, marker);
        }));

        // 마커 mouseout 시 인포창 숨기기
        markers.forEach((marker, idx) => kakao.maps.event.addListener(marker, 'mouseout', e => {
            infowindows[idx].close();
        }));

        //검색결과 목록 표시
        const wrapEle = document.querySelector('.map-wrap');
        const searchResultEle = makePlaceList(data, pagination, mymap.map, markers, infowindows);
        const isExist = document.querySelector('.search_result') ? true : false;

        if (!isExist) {
            wrapEle.appendChild(searchResultEle);
        } else {
            wrapEle.replaceChild(searchResultEle, document.querySelector('.search_result'));
        }

        const { lat, lng } = data.reduce((acc, place) => {
            acc.lat += parseFloat(place.y); //위도 누적
            acc.lng += parseFloat(place.x); //경도 누적
            return acc;
        }, { lat: 0, lng: 0 });
        console.log(lat, lng);

        // 위,경도 평균 계산
        const avgOfLat = lat / data.length;
        const avgOfLng = lng / data.length;

        console.log(avgOfLat, avgOfLng);
        mymap.map.setCenter(new N.LatLng(avgOfLat, avgOfLng));  // 중심 위치로 이동
        mymap.map.setZoom(16); //확대
    }
}

// 위치 정보를 가져오는 함수를 Promise로 래핑
function getCurrentPosition() {
    return new Promise((resolve, reject) => {
        window.navigator.geolocation.getCurrentPosition(resolve, reject);
    });
}

// 지도 초기화 함수
function initializeMap(lat, lng) {
    const mapOptions = {
        center: new N.LatLng(lat, lng),
        zoom: 15,
    };
    const map = new N.Map('map', mapOptions);
    mymap.map = map;
    return map;
}


// 마커 생성 함수
function makeMarker(map, lat, lng, iconHtml = null) {
    let marker = null;

    // icon html 마커 생성
    if (!!iconHtml && typeof iconHtml === 'string') {
        marker = new naver.maps.Marker({
            position: new naver.maps.LatLng(lat, lng),
            map: map,
            icon: {
                content: iconHtml,
                size: new naver.maps.Size(22, 35),
                anchor: new naver.maps.Point(11, 35)
            }
        });
        // 기본 마커 생성
    } else {
        marker = new naver.maps.Marker({
            position: new naver.maps.LatLng(lat, lng),
            map: map,
        });
    }

    return marker;
}

//case2)
// 마커 생성 함수
// function makeMarker(map, lat, lng, isMyLocation = false) {
//     const markerOptions = {
//         position: new naver.maps.LatLng(lat, lng),
//         map: map
//     };
//     // 내 위치 마커인 경우 아이콘 설정
//     if (isMyLocation) {
//         markerOptions.icon = {
//             content: `<div class="myMarker"></div>`,
//             size: new naver.maps.Size(22, 35),
//             anchor: new naver.maps.Point(11, 35)
//         };
//     }
//     const marker = new naver.maps.Marker(markerOptions);
//     return marker;
// }

//case3)
// // 내위치 마커 생성 함수 (my)
// function makeMyMarker(map, lat, lng) {
//     return new naver.maps.Marker({
//         position: new naver.maps.LatLng(lat, lng),
//         map: map,
//         icon: {
//             content: `<div class="myMarker"></div>`,
//             size: new naver.maps.Size(22, 35),
//             anchor: new naver.maps.Point(11, 35)
//         }
//     });
// }


// // 위치 마커 생성 함수
// function makeMarker(map, lat, lng) {
//     return new naver.maps.Marker({
//         position: new naver.maps.LatLng(lat, lng),
//         map: map
//     });
// }


//정보창 내용 생성 함수
function generateInfoWindowContent() {
    return `<div id='whereAmI'>
          <p><b>내위치</b></p>
          <p><a href='https://www.ulsankh.com/'>KH인재교육원</a></p>
        </div>`;
}

// 정보창 열기/닫기 토글 함수
function toggleInfoWindow(map, marker, infowindow) {
    //맵에 정보창 있으면 닫고 없으면 띄우기
    if (infowindow.getMap()) {
        infowindow.close();
    } else {
        infowindow.open(map, marker);
    }
}

//정보창 설정 함수
function setupInfoWindow(map, marker) {
    //정보창 문구 생성
    const contentString = generateInfoWindowContent();

    //정보창
    const infowindow = new naver.maps.InfoWindow({
        content: contentString,         //정보창에 보여줄 메시지
    });

    //정보창 띄우기
    infowindow.open(map, marker);

    //마커 클릭 이벤트 리스너 추가
    naver.maps.Event.addListener(marker, 'click', function (e) {
        toggleInfoWindow(map, marker, infowindow);
    });

}

//장소 검색 서비스 등록
function setupKaKaoServicesPlaces(map) {
    // 장소 검색 객체를 생성합니다

    const ps = new kakao.maps.services.Places(map);     //현재 맵을 기준으로 생성(map객체 전달)

    searchBtn.addEventListener('click', e => {
        // alert(keyword.value);
        // 키워드로 장소를 검색합니다

        const options = {
            // category_group_code String : 키워드 필터링을 위한 카테고리 코드
            location: new kakao.maps.LatLng(mymap.whereAmI.lat, mymap.whereAmI.lng),            // location LatLng : 중심 좌표. 특정 지역을 기준으로 검색한다.
            // x Number : x 좌표, longitude, location 값이 있으면 무시된다.
            // y Number : y 좌표, latitude, location 값이 있으면 무시된다.
            radius: 10000,           // radius Number : 중심 좌표로부터의 거리(반경) 필터링 값. location / x , y / useMapCenter 중 하나와 같이 써야 의미가 있음. 미터(m) 단위. 기본값은 5000, 0~20000까지 가능
            // bounds LatLngBounds : 검색할 사각형 영역
            // rect String : 사각 영역. 좌x,좌y,우x,우y 형태를 가짐. bounds 값이 있으면 무시된다.
            size: 15,              // size Number : 한 페이지에 보여질 목록 개수. 기본값은 15, 1~15까지 가능
            // page Number : 검색할 페이지. 기본값은 1, size 값에 따라 1~45까지 가능
            sort: kakao.maps.services.SortBy.DISTANCE           // sort SortBy : 정렬 옵션. DISTANCE 일 경우 지정한 좌표값에 기반하여 동작함. 기본값은 ACCURACY (정확도 순)
            // useMapCenter Boolean : 지정한 Map 객체의 중심 좌표를 사용할지의 여부. 참일 경우, location 속성은 무시된다. 기본값은 false
            // useMapBounds Boolean : 지정한 Map 객체의 영역을 사용할지의 여부. 참일 경우, bounds 속성은 무시된다. 기본값은 false
        };

        ps.keywordSearch(keyword.value, placesSearchCB, options);           //키워드에대한 검색결과(placesSerchCB : 콜백)

    }, false);
}

// 위치 정보를 가져오는 async함수
async function fetchLocation() {
  try {
    const position = await getCurrentPosition();
    const { latitude: lat, longitude: lng } = position.coords;
    console.log(`위도:${lat}, 경도:${lng}`);
    mymap.whereAmI.lat = lat;
    mymap.whereAmI.lng = lng;

    //지도 초기화
    const map = initializeMap(lat, lng);
    mymap.map = map;

    //내위치 마커표시
    const icon = `<div class='myMarker'></div>`
    const marker = makeMarker(map, lat, lng, icon);

    //정보창 설정
    setupInfoWindow(map, marker);

    //장소 검색 서비스 등록
    setupKaKaoServicesPlaces(map);

  } catch (err) {
    console.error('위치 정보를 가져오기 실패!', err);
  }
}

function main() {
    // 지도 초기화
    fetchLocation();
}
window.navermap_authFailure = () => {
    // 인증 실패 시 처리 코드 작성
    alert('인증실패');
};

