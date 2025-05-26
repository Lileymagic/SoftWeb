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
      const modal = document.querySelector(".modal1");
      
      if (!modal) {
        const div = document.createElement("div");
        div.innerHTML =  `<dialog class="modal1" style="overflow-y:scroll; ">
              <table class="table table-hover">
                  <thead>
                      <tr>
                          <th></th>
                          <th></th>
                      </tr>
                  </thead>
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
              <div style="display: flex; justify-content: flex-end; ">
                  <form method="dialog">
                      <button style="background-color:none; border:none;" class="font1">닫기</button>
                  </form>  
              </div>
          </dialog>`;
          document.body.appendChild(div);
          const closemodal = document.querySelector('.modal1');
          closeModal(closemodal);
      }
      
      const modalBtn = document.querySelector("#alert"); 
  
      modalBtn.addEventListener("click", () => { 
          modal.showModal(); 
      });
     
    }
  
    function openFriend() {
      const modal = document.querySelector(".modal2");
      if(!modal){
        const div = document.createElement("div");
        div.innerHTML=`
          <dialog class="modal2" style="border: none; background-color: transparent;">
              <div style="margin-top:30px;">
                  <div>
                      <button class="button_friend" onclick="Watchdiv('1')">친구 목록</button>
                  </div>
                  <div>
                      <button class="button_friend" onclick="Watchdiv('2')">유저 찾기</button>
                  </div>
                  <div>
                      <button class="button_friend" onclick="Watchdiv('3')">요청 대기</button>
                  </div>
              </div>
              <div>
              <div class="box_friend">
                  <div id="1">
                      <div class="green" style="text-align: center;">친구 목록</div>
                      <div style="width: 100%;">
                          <hr style="border: 1px solid rgb(0, 0, 0); "/>
                      </div>
                      <table class="table table-hover">
                          <thead>
                              <tr>
                                  <th><img src="/icon/user2.png" style="height: 25px; width: 25px; "/></th>
                                  <th>닉네임</th>
                                  <th>#태그</th>
                                  <th>마지막 접속시간</th>
                              </tr>
                          </thead>
                          <tbody>
                              <tr>
                                  <td>1</td>
                                  <td>1</td>
                                  <td>1</td>
                                  <td>1</td>
                              </tr>
                              <tr>
                                  <td>1</td>
                                  <td>1</td>
                                  <td>1</td>
                                  <td>1</td>
                              </tr>
                              <tr>
                                  <td>1</td>
                                  <td>1</td>
                                  <td>1</td>
                                  <td>1</td>
                              </tr>
                              <tr>
                                  <td>1</td>
                                  <td>1</td>
                                  <td>1</td>
                                  <td>1</td>
                              </tr>
                          </tbody>
                      </table>
                  </div>
  
                  <div id="2">
                      <div class="green" style="text-align: center;">유저 찾기</div>
                      <div style="width: 100%;">
                          <hr style="border: 1px solid rgb(0, 0, 0); "/>
                      </div>
                      <div style="margin-bottom: 10px; margin-left: 10px;">
                          <input class="button_find2" type="text" id="findid" placeholder="닉네임 #태그"/>
                          <img src="/icon/finding.png" style="height: 25px; width: 25px;" class="margin2 drag1"/>
                      </div>
                      <div style="margin-left: 10px;">
                          <div class="box8 ">
                              <table class="table table-hover">
                                  <thead>
                                      <tr>
                                          <th><img src="/icon/user2.png" style="height: 25px; width: 25px; margin:5px;"/></th>
                                          <th>닉네임</th>
                                          <th>#태그</th>
                                          <th><img src="/icon/adduser.png" style="height: 15px; width: 15px;"/></th>
                                      </tr>
                                  </thead>
                                  <tbody>
                                      <tr>
                                      <td>1</td>
                                      <td>1</td>
                                      <td>1</td>
                                      <td>1</td>
                                  </tr>
                                  <tr>
                                      <td>1</td>
                                      <td>1</td>
                                      <td>1</td>
                                      <td>1</td>
                                  </tr>
                                  <tr>
                                      <td>1</td>
                                      <td>1</td>
                                      <td>1</td>
                                      <td>1</td>
                                  </tr>
                                  <tr>
                                      <td>1</td>
                                      <td>1</td>
                                      <td>1</td>
                                      <td>1</td>
                                  </tr>
                                  </tbody>
                              </table>
                          </div>
                      </div>
                  </div>
                  <div id="3">
                      <div class="green" style="text-align: center;">요청 대기</div>
                      <div style="width: 100%;">
                          <hr style="border: 1px solid rgb(0, 0, 0); "/>
                      </div>
                      <table class="table table-hover">
                          <thead>
                              <tr>
                                  <th><img src="/icon/user2.png" style="height: 25px; width: 25px; margin:5px;"/></th>
                                  <th>닉네임</th>
                                  <th>#태그</th>
                                  <th>수락</th>
                                  <th>거절</th>
                              </tr>
                          </thead>
                          <tbody>
                              <tr>
                              <td>1</td>
                              <td>1</td>
                              <td>1</td>
                              <td><button style="background-color:rgb(55,158,144);">수락</button></td>
                              <td><button style="background-color:rgb(255,127,127);">거절</button></td>
                          </tr>
                          <tr>
                              <td>1</td>
                              <td>1</td>
                              <td>1</td>
                              <td><button style="background-color:rgb(55,158,144);">수락</button></td>
                              <td><button style="background-color:rgb(255,127,127);">거절</button></td>
                          </tr>
                          <tr>
                              <td>1</td>
                              <td>1</td>
                              <td>1</td>
                              <td><button style="background-color:rgb(55,158,144);">수락</button></td>
                              <td><button style="background-color:rgb(255,127,127);">거절</button></td>
                          </tr>
                          <tr>
                              <td>1</td>
                              <td>1</td>
                              <td>1</td>
                              <td><button style="background-color:rgb(55,158,144);">수락</button></td>
                              <td><button style="background-color:rgb(255,127,127);">거절</button></td>
                          </tr>
                          </tbody>
                      </table>
                  </div>
                </div>
              </div>
              <div style="display: flex; justify-content: flex-end; margin-top:450px; ">
                  <form method="dialog">
                      <button style="background-color:none; border:none;" class="font1">닫기</button>
                  </form>  
              </div>
          </dialog>`;
          document.body.appendChild(div);
          const closemodal = document.querySelector('.modal2');
          closeModal(closemodal);
      }
      const modalBtnFriend = document.querySelector("#friend");
      
      if (modalBtnFriend) { 
          modalBtnFriend.addEventListener("click", () => {
              const modal = document.querySelector(".modal2"); 
              if(modal) modal.showModal(); 
          });
      }
    }
  
    function Watchdiv(i) {
        const sections = ["1", "2", "3"];
        sections.forEach(id => {
            const el = document.getElementById(id);
            if (el) {
                el.style.display = (id === i) ? 'block' : 'none';
            }
        });
  
        const button = document.querySelectorAll('.button_friend');
        button.forEach((btn,index) => {
            if((index + 1).toString() === i){
                btn.classList.add('active');
            }
            else{
                btn.classList.remove('active');
            }
        });
    }
    window.addEventListener('load', () => Watchdiv('1'));
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
  
    function openMyPage() {
      const modal = document.querySelector(".modal3");
      if(!modal){
        const div = document.createElement("div");
        div.innerHTML=`
        <dialog class="modal3">
          <div class="middle3">
              <div style="display: flex; justify-content: flex-end;" >
                  <form method="dialog">
                      <button style="background-color:none; border:none;" class="font1">X</button>
                  </form>
              </div>
              <div style="display: flex;">
                  <img src="/icon/user2.png" style="height:100px; width:100px; margin:30px;"/>
                  <div style="margin:50px ;">
                      <div class="black">닉네임</div>
                      <div style="display: flex;">
                          <div style="color:green">아이디</div>
                          <div style="color:green">#태그</div>
                      </div>
                      <div>자기소개</div>
                  </div>
              </div>
              <div style="display: flex; justify-content: flex-end; margin:5px 5px 0 0; ">
                  <button class="button_pro" style="margin-right:5px;" onclick="window.location.href='/front/profile.html'; document.querySelector('.modal3').close();">프로필 수정</button>
                  <button class="button_pro" onclick="logout()">로그아웃</button>
              </div>
          </div>  
        </dialog>`;
        document.body.appendChild(div);
        const closemodal = document.querySelector('.modal3');
        closeModal(closemodal);
      }
  
      const modelBtn = document.querySelector("#mypage"); 
      if (modelBtn) { 
          modelBtn.addEventListener("click", () => { 
              const modalToOpen = document.querySelector(".modal3");
              if(modalToOpen) modalToOpen.showModal(); 
          });
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
        { className: 'right3 drag1 clear', onClick: openMyPage, id: 'mypage'},
        createElement('i', { className: 'fa-solid fa-user fa-2x', style: { fontSize: '25px'} })
      ),
      createElement(
        'button',
        { className: 'right2 drag1 clear', onClick: openFriend, id:'friend'},
        createElement('i', { className: 'fa-solid fa-user-group fa-2x', style: { fontSize: '25px'} })
      ),
      createElement(
        'button',
        { className: 'right1 drag1 clear', onClick: openAlert, id:'alert' },
        createElement('i', { className: 'fa-solid fa-bell fa-2x', style: { fontSize: '25px'}})
      )
    );
  }
    createRoot(document.getElementById('root')).render(createElement(approot));
    
    let target = document.querySelector("#root");
    if (target) { 
      let observer = new MutationObserver(() => {
          openAlert();
          openFriend();
          openMyPage();   
      });   
      window.addEventListener('load', function() {openAlert(); openFriend(); openMyPage();}); 
  
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