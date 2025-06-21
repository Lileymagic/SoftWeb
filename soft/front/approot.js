(function() {
    const { createElement, Fragment } = React;
    const { createRoot } = ReactDOM;
    
    function closeModal(dialog){
      dialog.addEventListener('click', function(e){
          if(e.target === dialog){
              dialog.close();
          }
      });
    }
    window.closeModal = closeModal;
  
    
    function openAlert() {
        let modal = document.querySelector(".modal1"); 
        
        if (!modal) {
          const div = document.createElement("div");
          div.innerHTML =  `
          <dialog class="modal1">
          <div class="wh100" style="display:flex; flex-direction: column;">
              <div style="padding: 10px; font-size: large; font-weight: bold;">알림</div>
              
              <hr style="margin: 0;">            
              <div class="box_scroll">
              <table class="table table-hover" style="overflow-y:auto;">
                  <tbody id="tbody_alert">
                      </tbody>
              </table>
              </div>
          <div>
          </dialog>`;
            document.body.appendChild(div);
            modal = document.querySelector('.modal1'); 
            if (modal) {
              closeModal(modal);
            }
        }
        const modalBtn = document.querySelector("#alertr"); 
  
          const updateModalPosition = () => {
              const rect = modalBtn.getBoundingClientRect();
              const modalRect = modal.getBoundingClientRect();
              modal.style.position = "fixed";
              modal.style.left = `${rect.right - modalRect.width}px`;            
              modal.style.top = `${rect.bottom}px`;
          };
  
          modalBtn.addEventListener("click", () => {
              modal.showModal();
              updateModalPosition();
              loadNotifications(); // 알림창을 열 때마다 알림을 새로고침합니다.
          });
  
          window.addEventListener("resize", () => {
              if (modal.open) {
                  updateModalPosition();
              }
          });
      }
  
       /**
     * 서버에서 알림 목록을 가져와 UI에 렌더링하는 메인 함수 (수정됨)
     */
    async function loadNotifications() {
        const tbody = document.getElementById("tbody_alert");
        if (!tbody) return;
        tbody.innerHTML = '<tr><td style="text-align:center; color:grey; padding:20px;">알림을 불러오는 중...</td></tr>';

        try {
            const response = await fetch('/api/notifications');
            if (!response.ok) throw new Error(`서버 오류: ${response.status}`);
            
            const result = await response.json();

            if (result.status === 200 && Array.isArray(result.data)) {
                tbody.innerHTML = ''; 
                if (result.data.length === 0) {
                    tbody.innerHTML = '<tr><td style="text-align:center; color:grey; padding:20px;">새로운 알림이 없습니다.</td></tr>';
                    return;
                }
                
                const notificationsToDisplay = result.data.slice(0, 30);
                notificationsToDisplay.forEach(notification => {
                    const tr = document.createElement('tr');
                    // *** 핵심 수정 포인트 ***
                    // 각 알림 행(tr)의 ID를 올바른 'idx' 값을 사용해 부여합니다.
                    tr.id = `notification-${notification.idx}`; 
                    
                    tr.innerHTML = renderNotification(notification);
                    tbody.appendChild(tr);
                });
            } else {
                tbody.innerHTML = `<tr><td style="text-align:center; color:red; padding:20px;">알림을 불러오지 못했습니다: ${result.message}</td></tr>`;
            }
        } catch (error) {
            console.error("알림 로딩 실패:", error);
            tbody.innerHTML = `<tr><td style="text-align:center; color:red; padding:20px;">알림 로딩 중 오류가 발생했습니다.</td></tr>`;
        }
    }

    /**
     * 알림 데이터 객체를 받아 종류에 맞는 HTML 문자열을 반환하는 함수 (최종본)
     */
    function renderNotification(noti) {
        const notificationId = noti.idx;
        const relatedId = noti.referenceIdx;
        const time = new Date(noti.createdAt).toLocaleString();
        let contentHtml = '';

        switch (noti.type) {
            case 'FRIEND_REQUEST':
                contentHtml = `
                    <div style="display: flex; flex-direction: row; align-items: center;">
                        <span>${noti.content}</span>
                        <div style="margin-left: auto; margin-right: 10px; display:flex; gap:5px;">
                            <button class="button_yes" onclick="handleFriendRequest(${relatedId}, true, ${notificationId})">수락</button>
                            <button class="button_no" onclick="handleFriendRequest(${relatedId}, false, ${notificationId})">거부</button>
                        </div>
                    </div>`;
                break;
            case 'PROJECT_INVITED':
                contentHtml = `
                    <div style="display: flex; flex-direction: row; align-items: center;">
                        <span>${noti.content}</span>
                        <div style="margin-left: auto; margin-right: 10px; display:flex; gap:5px;">
                            <button class="button_yes" onclick="handleProjectInvite(${relatedId}, true, ${notificationId})">수락</button>
                            <button class="button_no" onclick="handleProjectInvite(${relatedId}, false, ${notificationId})">거부</button>
                        </div>
                    </div>`;
                break;
            default:
                contentHtml = `<span>${noti.content}</span>`;
                break;
        }

        return `
            <td>
                <div style="display: flex; flex-direction: column; padding: 5px; gap: 8px;">
                    ${contentHtml}
                    <div style="display: flex; flex-direction: row; align-items: center;">
                        <span style="font-size: small; color: grey;">${time}</span>
                        <img src="/icon/trash.png" style="height: 20px; width: 20px; margin-left: auto; cursor:pointer;" onclick="deleteNotification(${notificationId})"/>
                    </div>
                </div>
            </td>`;
    }

    /**
     * 알림 삭제 API를 호출하고 성공 시 UI를 업데이트하는 함수 (최종본)
     */
    async function deleteNotification(notificationId) {
        if (!confirm("이 알림을 삭제하시겠습니까?")) return;

        try {
            const response = await fetch(`/api/notifications/${notificationId}`, { method: 'DELETE' });
            if (response.ok) {
                const notificationElement = document.getElementById(`notification-${notificationId}`);
                if (notificationElement) {
                    notificationElement.remove(); // 화면에서 해당 알림 요소 제거
                }

                const tbody = document.getElementById("tbody_alert");
                if (tbody && tbody.children.length === 0) {
                    tbody.innerHTML = '<tr><td style="text-align:center; color:grey; padding:20px;">새로운 알림이 없습니다.</td></tr>';
                }
            } else {
                const result = await response.json().catch(() => ({ message: "알림 삭제에 실패했습니다." }));
                throw new Error(result.message);
            }
        } catch (error) {
            console.error("알림 삭제 실패:", error);
            alert(`오류: ${error.message}`);
        }
    }
    window.deleteNotification = deleteNotification; // HTML onclick에서 호출 가능하도록 등록

    /**
     * '바로가기' 버튼 클릭 시, 해당 URL로 이동하고 알림을 읽음 처리하는 함수
     * @param {string} url - 이동할 경로
     * @param {number} notificationId - 읽음 처리할 알림의 ID
     */
    async function handleNotificationClick(url, notificationId) {
        try {
            // 알림을 읽음 처리
            await fetch(`/api/notifications/${notificationId}/read`, { method: 'POST' });
            // 관련 URL로 이동
            if (url) {
                window.location.href = url;
            }
        } catch (error) {
            console.error("알림 읽음 처리 또는 이동 중 오류:", error);
            // 에러가 발생해도 일단 이동은 시도
            if (url) {
                window.location.href = url;
            }
        }
    }
    window.handleNotificationClick = handleNotificationClick;

    /**
     * 친구 요청을 수락하거나 거절하는 함수
     * @param {number} requesterUserId - 요청을 보낸 사용자의 ID
     * @param {boolean} isAccepted - true면 수락, false면 거절
     * @param {number} notificationId - 관련된 알림 ID (처리 후 삭제용)
     */
    async function handleFriendRequest(requesterUserId, isAccepted, notificationId) {
        const action = isAccepted ? 'accept' : 'reject';
        const confirmMessage = isAccepted ? "친구 요청을 수락하시겠습니까?" : "친구 요청을 거절하시겠습니까?";
        if (!confirm(confirmMessage)) return;

        try {
            const response = await fetch(`/api/friends/${action}/${requesterUserId}`, { method: 'PATCH' });
            const result = await response.json();

            if (response.ok) {
                alert(`친구 요청을 ${isAccepted ? '수락' : '거절'}했습니다.`);
                // 성공 시 관련 알림 바로 삭제
                deleteNotification(notificationId);
                // 친구 목록 새로고침 (친구가 수락되었을 경우를 대비)
                if (isAccepted && typeof loadFriendList === 'function') {
                    loadFriendList();
                }
            } else {
                throw new Error(result.message || "요청 처리에 실패했습니다.");
            }
        } catch (error) {
            console.error("친구 요청 처리 실패:", error);
            alert(`오류: ${error.message}`);
        }
    }
    window.handleFriendRequest = handleFriendRequest;

    /**
     * 프로젝트 초대를 수락하거나 거절하는 함수
     * @param {number} projectId - 초대받은 프로젝트의 ID
     * @param {boolean} isAccepted - true면 수락, false면 거절
     * @param {number} notificationId - 관련된 알림 ID (처리 후 삭제용)
     */
    async function handleProjectInvite(projectId, isAccepted, notificationId) {
        const action = isAccepted ? 'accept' : 'reject';
        const confirmMessage = isAccepted ? "프로젝트 초대를 수락하시겠습니까?" : "프로젝트 초대를 거절하시겠습니까?";
        if (!confirm(confirmMessage)) return;

        try {
            const response = await fetch(`/api/projects/${projectId}/invitations/${action}`, { method: 'POST' });
             const result = await response.json();

            if (response.ok) {
                 alert(`프로젝트 초대를 ${isAccepted ? '수락' : '거절'}했습니다.`);
                 // 성공 시 관련 알림 바로 삭제
                 deleteNotification(notificationId);
                 // 수락했다면 프로젝트 목록 페이지로 이동하거나, 목록을 새로고침하는 로직 추가 가능
                 if(isAccepted) {
                    // 예시: 프로젝트 목록 페이지로 이동
                    // window.location.href = '/front/projectlist.html';
                 }
            } else {
                 throw new Error(result.message || "요청 처리에 실패했습니다.");
            }
        } catch (error) {
             console.error("프로젝트 초대 처리 실패:", error);
             alert(`오류: ${error.message}`);
        }
    }
    window.handleProjectInvite = handleProjectInvite;

    function openFriend() {
      let modal = document.querySelector(".modal2");
      if(!modal){
        const div = document.createElement("div");
        div.innerHTML=`
          <dialog class="modal2" style="border: none; background-color: transparent;">
          <div class="friend_w_box">
            <div class="position_modal2">
              <div>
                  <div>
                      <button class="button_friend" onclick="Watchdiv('friend1')">친구 목록</button>
                  </div>
                  <div>
                      <button class="button_friend" onclick="Watchdiv('friend2')">유저 찾기</button>
                  </div>
                  <div>
                      <button class="button_friend" onclick="Watchdiv('friend3')">요청 대기</button>
                  </div>
              </div>
              <div class="box_friend">
                  <div class="fccc wh100" id="friend1" style="display: none; overflow: hidden; padding: 20px;">
                      <div class="green" style="text-align: center;">친구 목록</div>
                      <div style="width: 100%; margin-bottom: 20px;">
                            <hr style="border: 1px solid rgb(0, 0, 0); "/>
                      </div>
                      <div class="box_scroll">
                        <table class="table table-hover" style="border-collapse: separate; border-spacing: 0;">
                            <thead>
                                <tr>
                                    <th style="width: 55px;"></th>
                                    <th style="width: 80px;">닉네임</th>
                                    <th style="width: 80px;">이메일</th> 
                                    <th style="width: 80px;">#태그</th>
                                    <th style="width: 100px;">관리</th>
                                </tr>
                            </thead>
                            <tbody id="friendListTbody"> 
                                
                            </tbody>
                        </table>
                        </div>
                  </div>

                  <div class="fccc wh100" id="friend2" style="display: none; padding: 20px;">
                    <div class="green">유저 찾기</div>
                    <div style="width: 100%; margin-bottom: 20px;">
                        <hr style="border: 1px solid rgb(0, 0, 0); "/>
                    </div>

                    <div class="find3" style="width: 100%; max-width: 450px;"> 
                        
                        <input class="button_find3" type="text" id="friendSearchQuery" placeholder="닉네임으로 유저 검색" style="font-size: small; width: 100%;"/>
                        <img src="/icon/finding.png" id="friendSearchBtn" style="width: 25px; height: 25px; margin: 5px; cursor:pointer;"/>
                    </div>

                    <div class="box10" style="height: 600px; width:100%; margin-top:15px;"> 
                    <div class="box_scroll">
                        <table class="table table-hover" style="border-collapse: separate; border-spacing: 0;">
                            <thead>
                                <tr>
                                    <th style="width: 55px;"></th>
                                    <th style="width: 100px;">닉네임</th>
                                    <th style="width: 100px;">이메일</th> 
                                    <th style="width: 60px;">#태그</th>
                                    <th style="width: 80px;">상태</th>
                                </tr>
                            </thead>
                            <tbody id="userSearchResultTbody"> 
                                
                            </tbody>
                        </table>
                    </div>
                    </div>
                  </div>
                  <div class="fccc wh100" id="friend3" style="display: none; overflow: hidden; padding: 20px;">
                      <div class="green" style="text-align: center;">받은 요청</div> 
                      <div style="width: 100%; margin-bottom: 20px;">
                          <hr style="border: 1px solid rgb(0, 0, 0); "/>
                      </div>
                      <div class="box_scroll">
                      <table class="table table-hover" style="border-collapse: separate; border-spacing: 0;">
                          <thead>
                              <tr>
                                  <th style="width: 55px;"></th>
                                  <th style="width: 80px;">닉네임</th>
                                  <th style="width: 80px;">이메일</th> 
                                  <th style="width: 80px;">#태그</th>
                                  <th style="width: 100px;">액션</th> 
                              </tr>
                          </thead>
                          <tbody id="receivedRequestsTbody">
                              
                          </tbody>
                      </table>
                      </div>
                  </div>
                </div>
              </div>
              <div style="display: flex; justify-content: flex-end; margin-top:450px; ">
                  <form method="dialog">
                      <button style="background-color:transparent; border:none;" class="font1">닫기</button>
                  </form>
              </div>
            </div>
        </div>
        </dialog>`;
        document.body.appendChild(div);
        modal = document.querySelector('.modal2');

        setTimeout(() => {
            Watchdiv('friend1'); // 기본으로 친구 목록 탭 표시
            loadFriendList();    // 친구 목록 로드
        }, 0);

        document.body.style.overflow = "hidden";
        if (modal) closeModal(modal);

        // 유저 찾기 탭의 검색 버튼/엔터키 이벤트 리스너 추가
        const friendSearchBtn = document.getElementById('friendSearchBtn');
        const friendSearchQueryInput = document.getElementById('friendSearchQuery');
        if (friendSearchBtn && friendSearchQueryInput) {
            friendSearchBtn.onclick = () => performUserSearch(friendSearchQueryInput.value.trim());
            friendSearchQueryInput.onkeypress = (e) => {
                if (e.key === 'Enter') {
                    performUserSearch(friendSearchQueryInput.value.trim());
                }
            };
        }
      } // openFriend 함수 끝

      const modalBtnFriend = document.querySelector("#friend");
      if (modalBtnFriend) {
          modalBtnFriend.addEventListener("click", () => {
              let currentModal = document.querySelector(".modal2");
              if(!currentModal) { // 모달이 없으면 생성
                  openFriend(); // 이 함수가 위에서 정의한 openFriend() 입니다.
                  currentModal = document.querySelector(".modal2");
              }
              if(currentModal) {
                currentModal.showModal();
                // 탭 전환 시 해당 탭의 데이터를 다시 로드하도록 Watchdiv 함수 내부에 로직 추가 필요
                Watchdiv('friend1'); // 기본 탭 표시
              }
          });
      }

      if(modal) { // modal 변수가 null이 아닐 때만 실행
            modal.addEventListener("close", () => {
                document.body.style.overflow = "auto";
            });
      }
    } // openFriend 정의 끝

    // Watchdiv 함수 수정: 탭 클릭 시 해당 목록을 로드하도록 변경
    function Watchdiv(i) {
        const sections = ["friend1", "friend2", "friend3"];
        sections.forEach(id => {
            const el = document.getElementById(id);
            if (el) {
                el.style.display = (id === i) ? 'block' : 'none';
            }
        });

        const button = document.querySelectorAll('.button_friend');
        button.forEach((btn,index) => {
            if("friend" + (index + 1).toString() === i){
                btn.classList.add('active');
            }
            else{
                btn.classList.remove('active');
            }
        });

        // 탭 전환 시 데이터 로드
        if (i === 'friend1') {
            loadFriendList();
        } else if (i === 'friend2') {
            // 유저 찾기 탭은 검색 시에만 데이터 로드하므로 여기서는 특별히 할 것 없음
            // 필요시 검색 결과 초기화
            const userSearchResultTbody = document.getElementById('userSearchResultTbody');
            if(userSearchResultTbody) userSearchResultTbody.innerHTML = '<tr><td colspan="5" style="text-align:center; color:grey;">닉네임으로 사용자를 검색하세요.</td></tr>';
        } else if (i === 'friend3') {
            loadReceivedFriendRequests();
        }
    }
    window.Watchdiv = Watchdiv;
    
    async function performUserSearch(query) {
        const userSearchResultTbody = document.getElementById('userSearchResultTbody');
        if (!userSearchResultTbody) return;

        if (!query) {
            userSearchResultTbody.innerHTML = '<tr><td colspan="5" style="text-align:center; color:grey;">검색어를 입력해주세요.</td></tr>';
            return;
        }
        userSearchResultTbody.innerHTML = '<tr><td colspan="5" style="text-align:center; color:grey;">검색 중...</td></tr>';

        try {
            // API 호출 시 query만 전달 (searchType 제거)
            const response = await fetch(`/api/friends/search?query=${encodeURIComponent(query)}`);
            if (!response.ok) {
                const errorData = await response.json().catch(() => ({ message: "검색 중 오류 발생" }));
                throw new Error(errorData.message || `Error: ${response.status}`);
            }
            const result = await response.json();

            if (result.status === 200 && Array.isArray(result.data)) {
                renderUserSearchResults(result.data);
            } else {
                userSearchResultTbody.innerHTML = `<tr><td colspan="5" style="text-align:center; color:red;">검색 결과를 가져오지 못했습니다: ${result.message}</td></tr>`;
            }
        } catch (error) {
            console.error("Error searching users:", error);
            userSearchResultTbody.innerHTML = `<tr><td colspan="5" style="text-align:center; color:red;">검색 중 오류: ${error.message}</td></tr>`;
        }
    }

    function renderUserSearchResults(users) {
        const userSearchResultTbody = document.getElementById('userSearchResultTbody');
        userSearchResultTbody.innerHTML = ''; // 이전 결과 지우기

        if (users.length === 0) {
            userSearchResultTbody.innerHTML = '<tr><td colspan="5" style="text-align:center; color:grey;">검색 결과가 없습니다.</td></tr>';
            return;
        }

        users.forEach(user => {
            const tr = document.createElement('tr');
            tr.innerHTML = `
                <td><img src="${user.profileImage || '/icon/user2.png'}" style="height: 30px; width: 30px; margin:5px; border-radius:50%; object-fit:cover;"/></td>
                <td class="lenCut_container"><span class="lenCut" title="${user.nickname}">${user.nickname}</span><div class="tooltip1"></div></td>
                <td><div class="lenCut_container"><span class="lenCutE" style="font-size: small; color: rgba(0, 0, 0, 0.5);" title="${user.email || ''}">${user.email || '-'}</span><div class="tooltip1"></div></div></td>
                <td><span style="color: #3a6b5b;">#${user.identificationCode}</span></td>
                <td id="actionCell_${user.userIdx}"></td>
            `;
            userSearchResultTbody.appendChild(tr);
            renderFriendActionButton(user.userIdx, user.friendStatus);
        });
        initializeTooltipsForAll(); // 새로 추가된 요소들에 대해 툴팁 초기화
    }

    function renderFriendActionButton(targetUserId, status) {
        const actionCell = document.getElementById(`actionCell_${targetUserId}`);
        if (!actionCell) return;
        actionCell.innerHTML = ''; // 기존 버튼 제거

        let button;
        switch (status) {
            case "NONE":
                button = document.createElement('button');
                button.className = 'button_select'; // 원본 CSS 클래스 사용
                button.style.width = '80px';
                button.textContent = '친구 신청';
                button.onclick = () => sendFriendRequestHandler(targetUserId);
                break;
            case "PENDING_SENT":
                button = document.createElement('button');
                button.className = 'button_sent'; // 원본 CSS 클래스 사용
                button.style.width = '80px';
                button.textContent = '요청 보냄';
                button.disabled = true;
                break;
            case "PENDING_RECEIVED":
                // 받은 요청은 "요청 대기" 탭에서 처리하므로 여기서는 "요청 받음" 등으로 표시하거나 버튼을 숨길 수 있음
                // 또는, "요청 대기 탭에서 확인하세요" 메시지 표시
                actionCell.innerHTML = '<span style="font-size:small; color:orange;">요청 받음</span>';
                return; // 버튼 추가 안 함
            case "ACCEPTED":
                actionCell.innerHTML = '<span style="font-size:small; color:green;">친구</span>';
                return; // 버튼 추가 안 함
            case "SELF":
                 actionCell.innerHTML = '<span style="font-size:small; color:grey;">본인</span>';
                return;
            default:
                actionCell.innerHTML = '-';
                return;
        }
        if(button) actionCell.appendChild(button);
    }

    async function sendFriendRequestHandler(recipientUserId) {
        if (!confirm("친구 요청을 보내시겠습니까?")) return;
        try {
            const response = await fetch('/api/friends/request', {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify({ recipientUserId: recipientUserId })
            });
            const result = await response.json();
            if (response.status === 201) {
                alert("친구 요청을 보냈습니다.");
                renderFriendActionButton(recipientUserId, "PENDING_SENT"); // 버튼 상태 업데이트
            } else {
                throw new Error(result.message || "친구 요청 실패");
            }
        } catch (error) {
            console.error("Error sending friend request:", error);
            alert(`오류: ${error.message}`);
        }
    }

    async function loadFriendList() {
        const friendListTbody = document.getElementById('friendListTbody');
        if (!friendListTbody) return;
        friendListTbody.innerHTML = '<tr><td colspan="5" style="text-align:center; color:grey;">친구 목록 로딩 중...</td></tr>';

        try {
            const response = await fetch('/api/friends');
            if (!response.ok) throw new Error(`Error: ${response.status}`);
            const result = await response.json();

            friendListTbody.innerHTML = '';
            if (result.status === 200 && Array.isArray(result.data)) {
                if (result.data.length === 0) {
                    friendListTbody.innerHTML = '<tr><td colspan="5" style="text-align:center; color:grey;">친구가 없습니다.</td></tr>';
                    return;
                }
                result.data.forEach(friend => { // friend는 UserSearchResponseDto 형태, friendStatus는 "ACCEPTED"
                    const tr = document.createElement('tr');
                    tr.innerHTML = `
                        <td><img src="${friend.profileImage || '/icon/user2.png'}" style="height: 30px; width: 30px; margin:5px; border-radius:50%; object-fit:cover;"/></td>
                        <td class="lenCut_container"><span class="lenCut" title="${friend.nickname}">${friend.nickname}</span><div class="tooltip1"></div></td>
                        <td><div class="lenCut_container"><span class="lenCutE" style="font-size: small; color: rgba(0, 0, 0, 0.5);" title="${friend.email || ''}">${friend.email || '-'}</span><div class="tooltip1"></div></div></td>
                        <td><span style="color: #3a6b5b;">#${friend.identificationCode}</span></td>
                        <td><button class="button_no" style="width:auto; padding:0 5px; height:25px; font-size:13px;" onclick="removeFriendHandler(${friend.userIdx}, '${friend.nickname}')">친구 삭제</button></td>
                    `;
                    friendListTbody.appendChild(tr);
                });
                initializeTooltipsForAll();
            } else {
                 friendListTbody.innerHTML = `<tr><td colspan="5" style="text-align:center; color:red;">친구 목록 로드 실패: ${result.message}</td></tr>`;
            }
        } catch (error) {
            console.error("Error loading friend list:", error);
            friendListTbody.innerHTML = `<tr><td colspan="5" style="text-align:center; color:red;">친구 목록 로드 오류.</td></tr>`;
        }
    }

    async function removeFriendHandler(friendUserId, friendNickname) {
        if (!confirm(`'${friendNickname}'님을 친구 목록에서 삭제하시겠습니까?`)) return;
        try {
            const response = await fetch(`/api/friends/${friendUserId}`, { method: 'DELETE' });
            if (response.ok) { // DELETE는 200 OK 또는 204 No Content
                alert("친구를 삭제했습니다.");
                loadFriendList(); // 친구 목록 새로고침
                // 만약 유저 검색 결과에도 영향이 있다면 해당 부분도 업데이트 필요
                const userSearchRowButton = document.querySelector(`#actionCell_${friendUserId} button`);
                if(userSearchRowButton && userSearchRowButton.textContent !== '친구 신청') { // 이미 친구였던 경우
                     renderFriendActionButton(friendUserId, "NONE"); // 버튼 상태를 "친구 신청"으로 변경
                }
            } else {
                const result = await response.json().catch(()=>({message: "친구 삭제 실패"}));
                throw new Error(result.message);
            }
        } catch (error) {
            console.error("Error removing friend:", error);
            alert(`오류: ${error.message}`);
        }
    }
    window.removeFriendHandler = removeFriendHandler;

    async function loadReceivedFriendRequests() {
        const receivedRequestsTbody = document.getElementById('receivedRequestsTbody');
        if (!receivedRequestsTbody) return;
        receivedRequestsTbody.innerHTML = '<tr><td colspan="5" style="text-align:center; color:grey;">받은 친구 요청 로딩 중...</td></tr>';

        try {
            const response = await fetch('/api/friends/requests/received');
            if (!response.ok) throw new Error(`Error: ${response.status}`);
            const result = await response.json();

            receivedRequestsTbody.innerHTML = '';
            if (result.status === 200 && Array.isArray(result.data)) {
                if (result.data.length === 0) {
                    receivedRequestsTbody.innerHTML = '<tr><td colspan="5" style="text-align:center; color:grey;">받은 친구 요청이 없습니다.</td></tr>';
                    return;
                }
                result.data.forEach(req => { // req는 FriendRequestResponseDto 형태
                    const user = req.user; // UserSearchResponseDto
                    const tr = document.createElement('tr');
                    tr.innerHTML = `
                        <td><img src="${user.profileImage || '/icon/user2.png'}" style="height: 30px; width: 30px; margin:5px; border-radius:50%; object-fit:cover;"/></td>
                        <td class="lenCut_container"><span class="lenCut" title="${user.nickname}">${user.nickname}</span><div class="tooltip1"></div></td>
                        <td><div class="lenCut_container"><span class="lenCutE" style="font-size: small; color: rgba(0, 0, 0, 0.5);" title="${user.email || ''}">${user.email || '-'}</span><div class="tooltip1"></div></div></td>
                        <td><span style="color: #3a6b5b;">#${user.identificationCode}</span></td>
                        <td>
                            <div style="display: flex; flex-direction: row; gap: 3px;">
                                <button class="button_yes" onclick="acceptFriendRequestHandler(${user.userIdx})">수락</button>
                                <button class="button_no" onclick="rejectFriendRequestHandler(${user.userIdx})">거절</button>
                            </div>
                        </td>
                    `;
                    receivedRequestsTbody.appendChild(tr);
                });
                initializeTooltipsForAll();
            } else {
                 receivedRequestsTbody.innerHTML = `<tr><td colspan="5" style="text-align:center; color:red;">받은 요청 목록 로드 실패: ${result.message}</td></tr>`;
            }
        } catch (error) {
            console.error("Error loading received requests:", error);
            receivedRequestsTbody.innerHTML = `<tr><td colspan="5" style="text-align:center; color:red;">받은 요청 목록 로드 오류.</td></tr>`;
        }
    }

    async function acceptFriendRequestHandler(requesterUserId) {
        try {
            const response = await fetch(`/api/friends/accept/${requesterUserId}`, { method: 'PATCH' });
            const result = await response.json();
            if (response.ok) {
                alert("친구 요청을 수락했습니다.");
                loadReceivedFriendRequests(); // 받은 요청 목록 새로고침
                loadFriendList(); // 친구 목록도 새로고침
            } else {
                throw new Error(result.message || "친구 요청 수락 실패");
            }
        } catch (error) {
            console.error("Error accepting friend request:", error);
            alert(`오류: ${error.message}`);
        }
    }
    window.acceptFriendRequestHandler = acceptFriendRequestHandler;

    async function rejectFriendRequestHandler(requesterUserId) {
        try {
            const response = await fetch(`/api/friends/reject/${requesterUserId}`, { method: 'PATCH' });
            const result = await response.json();
            if (response.ok) {
                alert("친구 요청을 거절했습니다.");
                loadReceivedFriendRequests(); // 받은 요청 목록 새로고침
            } else {
                throw new Error(result.message || "친구 요청 거절 실패");
            }
        } catch (error) {
            console.error("Error rejecting friend request:", error);
            alert(`오류: ${error.message}`);
        }
    }
    window.rejectFriendRequestHandler = rejectFriendRequestHandler;

    // 툴팁 초기화 함수 (전체적으로 적용하기 위해)
    function initializeTooltipsForAll() {
        document.querySelectorAll('.lenCut_container').forEach(container => {
            const spanText = container.querySelector(".lenCut");
            const spanTextE = container.querySelector(".lenCutE");
            const tooltip = container.querySelector('.tooltip1');

            const setupTooltip = (spanElement, maxLength) => {
                if (spanElement && tooltip) {
                    const originalText = spanElement.dataset.originalText || spanElement.textContent;
                    spanElement.dataset.originalText = originalText; // 원본 텍스트 저장

                    if (originalText.length > maxLength) {
                        spanElement.textContent = originalText.slice(0, maxLength) + "...";
                    } else {
                        spanElement.textContent = originalText;
                    }

                    spanElement.onmouseenter = function () {
                        if (spanElement.offsetWidth < spanElement.scrollWidth || spanElement.textContent.endsWith("...")) {
                            tooltip.style.display = 'block';
                            tooltip.textContent = originalText;
                        }
                    };
                    spanElement.onmouseleave = function () {
                        tooltip.style.display = 'none';
                    };
                }
            };
            setupTooltip(spanText, 6); // 일반 닉네임용 길이 제한
            setupTooltip(spanTextE, 15); // 이메일(또는 ID)용 길이 제한
        });
    }
    // 페이지 로드 시 및 동적 컨텐츠 추가 후 호출되도록 해야 함
    window.addEventListener('load', initializeTooltipsForAll);

    function logout(){
      const result = confirm("로그아웃 하겠습니까?");
      if(!result){
          return;
      }
      fetch('/api/auth/logout', { method: 'POST' })
          .then(response => {
              if(response.ok) {
                  window.location.href=('/front/login.html?logout');
              } else {
                  alert("로그아웃에 실패했습니다. 다시 시도해주세요.");
              }
          })
          .catch(error => {
              console.error("Logout error:", error);
              alert("로그아웃 중 오류가 발생했습니다.");
          });
    }
    window.logout = logout;
  
    function createMyPageModalIfNeeded() {
      let modal = document.querySelector(".modal3");
      if (!modal) {
        const div = document.createElement("div");
        div.innerHTML = `
        <dialog class="modal3">
        <div class="middle3">
                
            <div style="display: flex; justify-content: flex-end;" >
                <form method="dialog">
                    <button style="background-color:transparent; border:none;" class="font1">X</button>
                </form>
            </div>

            <div style="display: flex;  flex-direction: row;">

                <img id="myPageProfileImage" src="/icon/user2.png" style="height:100px; width:100px; margin:10px; margin-right: 30px; margin-left: 20px; border-radius: 50%;"/>

                <div style="display: flex; flex-direction: column; justify-content: center; padding: 5px; gap: 20px;">
                
                    <div style="display: flex;  flex-direction: column; gap: 5px;">
                        <div style="display: flex; gap: 5px; flex-direction: row; justify-content: flex-start; align-items: flex-end;">
                            <div class="black" id="myPageNickname" style="font-size: larger;">닉네임 로딩 중</div>
                            <div style="color:green" id="myPageTag">#0000</div>
                        </div>
                        <div id="myPageDisplayId" style="color:gray;">id 로딩 중...</div>
                        <div class="box_introduce" id="myPageBio">
                            자기소개 로딩 중...
                        </div>                    
                    </div>

                    <div style="display: flex; flex-direction: row; margin-left: auto;">
                        <button class="button_pro2" style="margin-right:5px; margin-left:7px;" onclick="document.querySelector('.modal3').close(); window.location.href='/front/profile.html';">프로필 수정</button>
                        <button class="button_pro2" onclick="logout()">로그아웃</button>
                    </div>  

                </div>
            </div> 

        </div>  
      </dialog>`;
        document.body.appendChild(div);
        modal = document.querySelector('.modal3');
        if (modal) {
            closeModal(modal);
        } else {
            console.error("Failed to create or find .modal3 for mypage.");
        }
      }
        const modalBtn = document.querySelector("#mypage");

        const updateModalPosition = () => {
            const rect = modalBtn.getBoundingClientRect();
            const modalRect = modal.getBoundingClientRect();
            modal.style.position = "fixed";
            modal.style.left = `${rect.right - modalRect.width}px`;
            modal.style.top = `${rect.bottom}px`;
        };

        modalBtn.addEventListener("click", () => {
        modal.showModal();
        updateModalPosition();
        });

        window.addEventListener("resize", () => {
            if (modal.open) {
                updateModalPosition();
            }
        });

        return modal;
    }
    

    

    async function fetchAndDisplayMyPageInfo() {
      const modalDisplayId = document.getElementById('myPageDisplayId');
      const modalNickname = document.getElementById('myPageNickname');
      const modalTag = document.getElementById('myPageTag');
      const modalBio = document.getElementById('myPageBio');
      const modalProfileImage = document.getElementById('myPageProfileImage');

      if (!modalDisplayId || !modalNickname || !modalTag || !modalBio || !modalProfileImage) {
          console.error("MyPage modal elements not found. Ensure createMyPageModalIfNeeded ran successfully.");
          return;
      }

      try {
        const response = await fetch('/api/users/me'); 
        if (!response.ok) {
          if (response.status === 401) { 
            modalDisplayId.textContent = "로그인 필요";
            modalNickname.textContent = "";
            modalTag.textContent = "";
            modalBio.textContent = "로그인 후 이용해주세요.";
            return;
          }
          throw new Error(`Failed to fetch user info: ${response.status}`);
        }

        const result = await response.json();
        const user = result.data || result; 

        if (user) {
          if (user.loginId && user.loginId.trim() !== "") { 
            modalDisplayId.textContent = user.loginId;
          } else { 
            modalDisplayId.textContent = "소셜 로그인";
          }
          modalNickname.textContent = user.nickname || "닉네임 정보 없음";
          modalTag.textContent = user.identificationCode ? `#${user.identificationCode}` : "";
          modalBio.textContent = user.bio || "자기소개가 없습니다.";
          modalProfileImage.src = user.profileImage || "/icon/user2.png";
        } else {
          console.error("User info data is not in expected format:", result);
          modalDisplayId.textContent = "정보 형식 오류";
          modalNickname.textContent = "";
          modalTag.textContent = "";
        }
      } catch (error) {
          console.error("Error fetching user info:", error);
          modalDisplayId.textContent = "정보 로드 오류";
          modalNickname.textContent = "";
          modalTag.textContent = "";
          modalBio.textContent = "정보를 불러오는 데 실패했습니다.";
      }
    }

    async function handleMyPageClick() {
      let modalInstance = createMyPageModalIfNeeded();
      if (modalInstance) {
        await fetchAndDisplayMyPageInfo();
        modalInstance.showModal();
      } else {
        console.error("Mypage modal (.modal3) could not be initialized for showing.");
      }
    }

    function openProjectList() {
      window.location.href=('/front/projectlist.html');
    }
  
    function approot() {
      return createElement(
        'div',
        { className: 'root' },
        createElement(
          'button',
          { className: 'left1 drag1 clear', onClick: openProjectList},
          createElement('img', { src: '/icon/logo.png', className: 'logo', style: {height: '50px'} })
        ),
        createElement(
          'button',
          { className: 'right3 drag1 clear', onClick: handleMyPageClick, id: 'mypage'},
          createElement('i', { className: 'fa-solid fa-user fa-2x', style: { fontSize: '25px'} })
        ),
        createElement(
          'button',
          { className: 'right2 drag1 clear', 
            onClick: () => { 
              let friendModal = document.querySelector(".modal2"); 
              if (!friendModal) { openFriend(); friendModal = document.querySelector(".modal2");} 
              if (friendModal) friendModal.showModal();
            }, 
            id:'friend'
          },
          createElement('i', { className: 'fa-solid fa-user-group fa-2x', style: { fontSize: '25px'} })
        ),
        createElement(
          'button',
          { className: 'right1 drag1 clear', onClick: openAlert , id:'alertr' 
          },
          createElement('i', { className: 'fa-solid fa-bell fa-2x', style: { fontSize: '25px'}})
        )
      );
    }
    createRoot(document.getElementById('root')).render(createElement(approot));
    
    function initializeAppModals() {
        if (!document.querySelector(".modal1")) openAlert();
        if (!document.querySelector(".modal2")) openFriend();
        createMyPageModalIfNeeded();
    }
    
    window.addEventListener('load', initializeAppModals); 
  
    let target = document.querySelector("#root");
    if (target) { 
      let observer = new MutationObserver(initializeAppModals);   
      let option = { attributes: true, childList: true, characterData: true };
      observer.observe(target, option);
    }
  
    document.querySelectorAll('.lenCut_container').forEach(container => {
      const spanText = container.querySelector(".lenCut");
      const spanTextE = container.querySelector(".lenCutE");
      const tooltip = container.querySelector('.tooltip1');
  
      if (spanText) {
          const spanTextOriginal = spanText.textContent;
          if ( spanTextOriginal.length > 6) {
              spanText.textContent = spanTextOriginal.slice(0, 6) + "...";
              if (tooltip) {
                  spanText.addEventListener('mouseenter', function () {
                      tooltip.style.display = 'block';
                      tooltip.textContent = spanTextOriginal;
                  });
                  spanText.addEventListener('mouseleave', function () {
                      tooltip.style.display = 'none';
                  });         
              }
          }
      }
  
      if (spanTextE){
          const spanTextOriginalE = spanTextE.textContent;
          if ( spanTextOriginalE.length > 15) {
              spanTextE.textContent = spanTextOriginalE.slice(0, 15) + "...";
              if (tooltip) {
                  spanTextE.addEventListener('mouseenter', function () {
                      tooltip.style.display = 'block';
                      tooltip.textContent = spanTextOriginalE;
                  });
                  spanTextE.addEventListener('mouseleave', function () {
                      tooltip.style.display = 'none';
                  });         
              }
          }         
      }
    });
  
    const btnCheck = document.querySelectorAll(".button_select");
    btnCheck.forEach((btn) => {
      let isActive = false;
  
      btn.addEventListener('click', () => {
          isActive = !isActive;
          btn.classList.toggle('active');
  
          if (isActive) {
              btn.textContent = '취소';
          } else {
              btn.textContent = '선택';
          }
      });
  
      btn.addEventListener('mouseenter', () => {
          if (isActive) {
              btn.textContent = '취소';
          }
      });
      btn.addEventListener('mouseleave', () => {
          if (isActive) {
              btn.textContent = '선택';
          }
      });
    });
  })();