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
        div.innerHTML =  `<dialog class="modal1">
                <div class="box_scroll">
                <table class="table table-hover" style="overflow-y:auto;">
                    <tbody>
                        <tr>
                            <td>
                                <div style="display: flex;">
                                    <div>프로젝트 1에 업무가 추가되었습니다.</div>
                                </div>
                                <br/>
                                <div style="display: flex;">
                                    <div style="font-size: small;">2025/05/05 12:00</div>
                                </div>
                            </td>
                            <td>
                                <div>
                                    <img src="/icon/trash.png" style="height: 20px; width: 20px; margin-top:55px;"/>
                                </div>
                            </td>
                        </tr>
                        <tr>
                            <td>
                                <div style="display: flex;">
                                    <div>QWER님께서 친구를 요청합니다.</div>
                                    <button style="margin-left:10px;" class="button_ale">수락</button>
                                    <button style="margin-left:5px;" class="button_ale">거부</button>
                                </div>
                                <br/>
                                <div style="display: flex;">
                                    <div style="font-size: small;">2025/05/05 12:00</div>
                                </div>
                            </td>
                            <td>
                                <div>
                                    <img src="/icon/trash.png" style="height: 20px; width: 20px; margin-top:55px;"/>
                                </div>
                            </td> 
                        </tr>
                        <tr>
                            <td>
                                <div style="display: flex;">
                                    <div>프로젝트 2에 초대되었습니다.</div>
                                    <div style="margin-left:10px;">(초대 유저: asdf12)</div>
                                    <button style="margin-left:10px;" class="button_ale">수락</button>
                                    <button style="margin-left:5px;" class="button_ale">거부</button>
                                </div>
                                <br/>
                                <div style="display: flex;">
                                    <div style="font-size: small;">2025/05/05 12:00</div>
                                </div>
                            </td>
                            <td>
                                <div>
                                    <img src="/icon/trash.png" style="height: 20px; width: 20px; margin-top:55px;"/>
                                </div>
                            </td> 
                        </tr>
                        <tr>
                            <td>
                                <div style="display: flex;">
                                    <div>프로젝트 1에 업무가 추가되었습니다.</div>
                                </div>
                                <br/>
                                <div style="display: flex;">
                                    <div style="font-size: small;">2025/05/05 12:00</div>
                                </div>
                            </td>
                            <td>
                                <div>
                                    <img src="/icon/trash.png" style="height: 20px; width: 20px; margin-top:55px;"/>
                                </div>
                            </td>
                        </tr>
                        <tr>
                            <td>
                                <div style="display: flex;">
                                    <div>프로젝트 2에 업무가 추가되었습니다.</div>
                                </div>
                                <br/>
                                <div style="display: flex;">
                                    <div style="font-size: small;">2025/05/05 12:00</div>
                                </div>
                            </td>
                            <td>
                                <div>
                                    <img src="/icon/trash.png" style="height: 20px; width: 20px; margin-top:55px;"/>
                                </div>
                            </td>
                        </tr>
                        <tr>
                            <td>
                                <div style="display: flex;">
                                    <div>스크롤 확인용1</div>
                                </div>
                                <br/>
                                <div style="display: flex;">
                                    <div style="font-size: small;">2025/05/05 12:00</div>
                                </div>
                            </td>
                            <td>
                                <div>
                                    <img src="/icon/trash.png" style="height: 20px; width: 20px; margin-top:55px;"/>
                                </div>
                            </td>
                        </tr>
                        <tr>
                            <td>
                                <div style="display: flex;">
                                    <div>스크롤 확인용1</div>
                                </div>
                                <br/>
                                <div style="display: flex;">
                                    <div style="font-size: small;">2025/05/05 12:00</div>
                                </div>
                            </td>
                            <td>
                                <div>
                                    <img src="/icon/trash.png" style="height: 20px; width: 20px; margin-top:55px;"/>
                                </div>
                            </td>
                        </tr>
                        <tr>
                            <td>
                                <div style="display: flex;">
                                    <div>스크롤 확인용1</div>
                                </div>
                                <br/>
                                <div style="display: flex;">
                                    <div style="font-size: small;">2025/05/05 12:00</div>
                                </div>
                            </td>
                            <td>
                                <div>
                                    <img src="/icon/trash.png" style="height: 20px; width: 20px; margin-top:55px;"/>
                                </div>
                            </td>
                        </tr>
                        <tr>
                            <td>
                                <div style="display: flex;">
                                    <div>스크롤 확인용1</div>
                                </div>
                                <br/>
                                <div style="display: flex;">
                                    <div style="font-size: small;">2025/05/05 12:00</div>
                                </div>
                            </td>
                            <td>
                                <div>
                                    <img src="/icon/trash.png" style="height: 20px; width: 20px; margin-top:55px;"/>
                                </div>
                            </td>
                        </tr>
                    </tbody>
                </table>
                </div>
                <div style="display: flex; justify-content: flex-end; ">
                    <form method="dialog">
                        <button style="background-color:transparent; border:none;" class="font1">닫기</button>
                    </form>  
                </div>
            </dialog>`;
          document.body.appendChild(div);
          modal = document.querySelector('.modal1'); 
          if (modal) closeModal(modal); 
      }
      
      const modalBtn = document.querySelector("#alert"); 

      // 창 새로고침 후 3초 정도 후에 알림창 열면 위치 정보를 못받아 잘못된 위치에 알림창이 이동하는 문제가 발생. (해결 필요)
      // (즉시 열면 괜찮음)
        const updateModalPosition = () => {
            const rect = modalBtn.getBoundingClientRect();
            const modalRect = modal.getBoundingClientRect();
            modal.style.position = "fixed";
            if (rect.right - modalRect.width > 0) {
                modal.style.left = `${rect.right - modalRect.width}px`;            
            }
            modal.style.top = `${rect.bottom}px`;
        };

        if (modalBtn) { 
            modalBtn.addEventListener("click", () => {
                const currentModal = document.querySelector(".modal1"); 
                if(currentModal) {
                    currentModal.showModal();
                    updateModalPosition();
                }
            });
        }

        window.addEventListener("resize", () => {
            if (modal.open) {
                updateModalPosition();
            }
        });
    }
  
    function openFriend() {
      let modal = document.querySelector(".modal2"); 
      if(!modal){
        const div = document.createElement("div");
        div.innerHTML=`
          <dialog class="modal2" style="border: none; background-color: transparent;">
            <div class="position_modal2">
              <div>
                  <div>
                      <button class="button_friend" style="border-bottom-left-radius: 0px; border-bottom-right-radius: 0px;" onclick="Watchdiv('friend1')">친구 목록</button>
                  </div>
                  <div>
                      <button class="button_friend" style="border-radius: 0px;" onclick="Watchdiv('friend2')">유저 찾기</button>
                  </div>
                  <div>
                      <button class="button_friend" style="border-top-left-radius: 0px; border-top-right-radius: 0px;" onclick="Watchdiv('friend3')">요청 대기</button>
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
                                    <th style="width: 80px;">아이디</th>
                                    <th style="width: 80px;">#태그</th>
                                    <th style="width: 80px;">접속</th>
                                    <!-- <img src="icon/plus.png" style="height: 15px; width: 15px;"/> -->
                                </tr>
                            </thead>
                            <tbody>
                                <tr>
                                    <td>
                                        <img src="icon/user2.png" style="height: 30px; width: 30px; margin:5px;"/>
                                    </td>
                                    <td class="lenCut_container">
                                        <span class="lenCut">닉네임</span>
                                        <div class="tooltip1" id="tooltip"></div>                                                      
                                    </td>
                                    <td>
                                        <div class="lenCut_container">
                                            <span class="lenCutE" style="font-size: small; color: rgba(0, 0, 0, 0.5);">identification123</span>                            
                                            <div class="tooltip1" id="tooltip"></div>                                        
                                        </div>                            
                                    </td>
                                    <td>
                                        <span style="color: #3a6b5b;">#0000</span>                               
                                    </td>  
                                    <td>
                                        <span style="font-size: small; color: rgba(0, 0, 0, 0.5);";>2025-05-27</span>
                                    </td>                           
                                </tr>
                                <tr>
                                    <td>
                                        <img src="icon/user2.png" style="height: 30px; width: 30px; margin:5px;"/>
                                    </td>
                                    <td class="lenCut_container">
                                        <span class="lenCut">고구마</span>
                                        <div class="tooltip1" id="tooltip"></div>                                                      
                                    </td>
                                    <td>
                                        <div class="lenCut_container">
                                            <span class="lenCutE" style="font-size: small; color: rgba(0, 0, 0, 0.5);">goguma04</span>                            
                                            <div class="tooltip1" id="tooltip"></div>                                        
                                        </div>                            
                                    </td>
                                    <td>
                                        <span style="color: #3a6b5b;">#0000</span>                               
                                    </td>  
                                    <td>
                                        <span style="font-size: small; color: rgba(0, 0, 0, 0.5);";>접속중</span>
                                    </td>                           
                                </tr>
                                <tr><td>
                                    스크롤테스트용ㅁㅁㅁㅁㅁㅁㅁㅁㅁㅁㅁㅁㅁㅁㅁㅁㅁㅁㅁㅁㅁㅁㅁㅁㅁㅁㅁㅁㅁㅁㅁㅁㅁㅁㅁㅁㅁㅁㅁㅁㅁㅁㅁㅁㅁㅁㅁㅁㅁㅁㅁㅁㅁㅁㅁㅁㅁㅁㅁ
                                    ㅁㅁㅁㅁㅁㅁㅁㅁㅁㅁㅁㅁㅁㅁㅁㅁㅁㅁㅁㅁㅁㅁㅁㅁㅁㅁㅁㅁㅁㅁㅁㅁㅁㅁㅁㅁㅁㅁㅁㅁㅁㅁㅁㅁㅁㅁㅁㅁㅁㅁㅁㅁㅁㅁㅁㅁㅁㅁㅁㅁㅁㅁㅁㅁㅁㅁ
                                </td></tr>
                            </tbody>
                        </table>
                        </div>
                  </div>
  
                  <div class="fccc wh100" id="friend2" style="display: none; padding: 20px;">
                    <div class="green">유저 찾기</div>
                    <div style="width: 100%; margin-bottom: 20px;">
                        <hr style="border: 1px solid rgb(0, 0, 0); "/>
                    </div>
                
                    <div class="find3" style="width: 500px">
                        <input class="button_find3" type="text" id="find" placeholder="아이디 / 태그로 유저 검색" style="font-size: small; width: 400px;"/> 
                        <img src="icon/finding.png" style="width: 25px; height: 25px; margin: 5px; margin-left: auto;"/>                    
                    </div>        
            
                    <div class="box10" style="height: 550px;">
                    <div class="box_scroll">
                        <table class="table table-hover" style="border-collapse: separate; border-spacing: 0;">
                            <thead>
                                <tr>
                                    <th style="width: 55px;"></th>
                                    <th style="width: 100px;">닉네임</th>
                                    <th style="width: 100px;">아이디</th>
                                    <th style="width: 60px;">#태그</th>
                                    <th style="width: 80px;"></th>
                                    <!-- <img src="icon/plus.png" style="height: 15px; width: 15px;"/> -->
                                </tr>
                            </thead>
                            <tbody>
                                <tr>
                                    <td>
                                        <img src="icon/user2.png" style="height: 30px; width: 30px; margin:5px;"/>
                                    </td>
                                    <td class="lenCut_container">
                                        <span class="lenCut">닉네임이아주아주아주길어요</span>
                                        <div class="tooltip1" id="tooltip"></div>                                                      
                                    </td>
                                    <td>
                                        <div class="lenCut_container">
                                            <span class="lenCutE" style="font-size: small; color: rgba(0, 0, 0, 0.5);">identification123</span>                            
                                            <div class="tooltip1" id="tooltip"></div>                                        
                                        </div>                            
                                    </td>
                                    <td>
                                        <span style="color: #3a6b5b;">#0000</span>                               
                                    </td>  
                                    <td>
                                        <button class="button_select" style="width: 80px;">친구 신청</button>
                                    </td>                           
                                </tr>
                                <tr>
                                    <td>
                                        <img src="icon/user2.png" style="height: 30px; width: 30px; margin:5px;"/>
                                    </td>
                                    <td class="lenCut_container">
                                        <span class="lenCut">정예은</span>
                                        <div class="tooltip1" id="tooltip"></div>                                                      
                                    </td>
                                    <td>
                                        <div class="lenCut_container">
                                            <span class="lenCutE" style="font-size: small; color: rgba(0, 0, 0, 0.5);">yeeun13</span>                            
                                            <div class="tooltip1" id="tooltip"></div>                                        
                                        </div>                            
                                    </td>
                                    <td>
                                        <span style="color: #3a6b5b;">#3697</span>                               
                                    </td>  
                                    <td>
                                        <button class="button_sent" style="width: 80px; " >요청 보냄</button>
                                    </td>                           
                                </tr>
                                <tr><td>
                                    스크롤 테스트용 아무말 ㅁㅁㅁㅁㅁㅁㅁㅁㅁㅁㅁㅁㅁㅁㅁㅁㅁㅁㅁㅁㅁㅁㅁㅁㅁㅁㅁㅁㅁㅁㅁㅁㅁㅁㅁㅁㅁㅁㅁㅁㅁㅁㅁ
                                    ㅁㅁㅁㅁㅁㅁㅁㅁㅁㅁㅁㅁㅁㅁㅁㅁㅁㅁㅁㅁㅁㅁㅁㅁㅁㅁㅁㅁㅁㅁㅁㅁㅁㅁㅁㅁㅁㅁㅁㅁㅁㅁㅁㅁㅁㅁㅁㅁㅁㅁㅁㅁㅁㅁㅁ
                                </td></tr>
                            </tbody>
                        </table>
                    </div>
                    </div>
                  </div>
                  <div class="fccc wh100" id="friend3" style="display: none; overflow: hidden; padding: 20px;">
                      <div class="green" style="text-align: center;">요청 대기</div>
                      <div style="width: 100%; margin-bottom: 20px;">
                          <hr style="border: 1px solid rgb(0, 0, 0); "/>
                      </div>
                      <div class="box_scroll">
                      <table class="table table-hover" style="border-collapse: separate; border-spacing: 0;">
                          <thead>
                              <tr>
                                  <th style="width: 55px;"></th>
                                  <th style="width: 80px;">닉네임</th>
                                  <th style="width: 80px;">아이디</th>
                                  <th style="width: 80px;">#태그</th>
                                  <th style="width: 80px;"></th>
                              </tr>
                          </thead>
                          <tbody>
                              <tr>
                                  <td>
                                      <img src="icon/user2.png" style="height: 30px; width: 30px; margin:5px;"/>
                                  </td>
                                  <td class="lenCut_container">
                                      <span class="lenCut">닉네임</span>
                                      <div class="tooltip1" id="tooltip"></div>                                                      
                                  </td>
                                  <td>
                                      <div class="lenCut_container">
                                          <span class="lenCutE" style="font-size: small; color: rgba(0, 0, 0, 0.5);">identification123</span>                            
                                          <div class="tooltip1" id="tooltip"></div>                                        
                                      </div>                            
                                  </td>
                                  <td>
                                      <span style="color: #3a6b5b;">#0000</span>                               
                                  </td>  
                                  <td>
                                    <div style="display: flex; flex-direction: row; gap: 3px;">
                                      <button class="button_yes">수락</button>
                                      <button class="button_no">거절</button>
                                    </div>
                                  </td>                           
                              </tr>
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
          </dialog>`;
        document.body.appendChild(div);
        modal = document.querySelector('.modal2');

        setTimeout(() => {
            Watchdiv('friend1');
        }, 0);

        if (modal) closeModal(modal); 
      }
      const modalBtnFriend = document.querySelector("#friend");
      
      if (modalBtnFriend) { 
          modalBtnFriend.addEventListener("click", () => {
              const currentModal = document.querySelector(".modal2"); 
              if(currentModal) {
                currentModal.showModal();
                setTimeout(() => {
                    Watchdiv('friend1');
                }, 0)
            }
          });
      }
    }
  
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
    }
    // window.addEventListener('load', () => Watchdiv('1'));
    window.Watchdiv = Watchdiv;
    
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

            <div style="display: flex;">
                <img id="myPageProfileImage" src="/icon/user2.png" style="height:100px; width:100px; margin:10px; margin-right: 30px; margin-left: 20px border-radius: 50%;"/>
                <div style="display: flex; flex-direction: column; justify-content: center; padding: 5px;">
                    <div style="display: flex; gap: 5px;">
                        <div class="black" id="myPageNickname" style="font-size: larger;">닉네임 로딩 중</div>
                        <div style="color:green" id="myPageTag">#0000</div>
                    </div>
                    <div id="myPageDisplayId" style="color:gray;">id 로딩 중...</div>
                    <div class="box_introduce" id="myPageBio">
                        자기소개 로딩 중...
                    </div>

                    <div style="display: flex; flex-direction: row; margin-top: 30px;">
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
          { className: 'right1 drag1 clear', 
            onClick: () => { 
              let alertModal = document.querySelector(".modal1"); 
              if (!alertModal) { openAlert(); alertModal = document.querySelector(".modal1"); }
              if (alertModal) alertModal.showModal();
            }, 
            id:'alert' 
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