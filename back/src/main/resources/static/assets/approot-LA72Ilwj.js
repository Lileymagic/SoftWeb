(function(){const y=document.createElement("link").relList;if(y&&y.supports&&y.supports("modulepreload"))return;for(const i of document.querySelectorAll('link[rel="modulepreload"]'))a(i);new MutationObserver(i=>{for(const l of i)if(l.type==="childList")for(const c of l.addedNodes)c.tagName==="LINK"&&c.rel==="modulepreload"&&a(c)}).observe(document,{childList:!0,subtree:!0});function f(i){const l={};return i.integrity&&(l.integrity=i.integrity),i.referrerPolicy&&(l.referrerPolicy=i.referrerPolicy),i.crossOrigin==="use-credentials"?l.credentials="include":i.crossOrigin==="anonymous"?l.credentials="omit":l.credentials="same-origin",l}function a(i){if(i.ep)return;i.ep=!0;const l=f(i);fetch(i.href,l)}})();(function(){const{createElement:r,Fragment:y}=React,{createRoot:f}=ReactDOM;function a(t){t.addEventListener("click",function(e){e.target===t&&t.close()})}window.closeModal=a;function i(){let t=document.querySelector(".modal1");if(!t){const d=document.createElement("div");d.innerHTML=`<dialog class="modal1" style="overflow-y:scroll; ">
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
            </dialog>`,document.body.appendChild(d),t=document.querySelector(".modal1"),t&&a(t)}const e=document.querySelector("#alert");e&&e.addEventListener("click",()=>{const d=document.querySelector(".modal1");d&&d.showModal()})}function l(){let t=document.querySelector(".modal2");if(!t){const d=document.createElement("div");d.innerHTML=`
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
          </dialog>`,document.body.appendChild(d),t=document.querySelector(".modal2"),t&&a(t)}const e=document.querySelector("#friend");e&&e.addEventListener("click",()=>{const d=document.querySelector(".modal2");d&&d.showModal()})}function c(t){["1","2","3"].forEach(o=>{const n=document.getElementById(o);n&&(n.style.display=o===t?"block":"none")}),document.querySelectorAll(".button_friend").forEach((o,n)=>{(n+1).toString()===t?o.classList.add("active"):o.classList.remove("active")})}window.addEventListener("load",()=>c("1")),window.Watchdiv=c;function h(){confirm("로그아웃 하겠습니까?")&&fetch("/api/auth/logout",{method:"POST"}).then(e=>{e.ok?window.location.href="/front/login.html?logout":alert("로그아웃에 실패했습니다. 다시 시도해주세요.")}).catch(e=>{console.error("Logout error:",e),alert("로그아웃 중 오류가 발생했습니다.")})}window.logout=h;function g(){let t=document.querySelector(".modal3");if(!t){const e=document.createElement("div");e.innerHTML=`
        <dialog class="modal3" style="position: fixed; top: 50%; left: 50%; transform: translate(-50%, -50%); border-radius: 10px; padding: 20px; box-shadow: 0 5px 15px rgba(0,0,0,0.3);">
          <div class="middle3">
              <div style="display: flex; justify-content: flex-end;" >
                  <form method="dialog">
                      <button style="background-color:transparent; border:none; font-size: 1.2em; cursor: pointer;" class="font1">X</button>
                  </form>
              </div>
              <div style="display: flex;">
                  <img id="myPageProfileImage" src="/icon/user2.png" style="height:100px; width:100px; margin:30px; border-radius: 50%;"/>
                  <div style="margin:50px ;">
                      <div id="myPageDisplayId" class="black" style="margin-bottom: 2px; font-weight: bold;">ID 로딩 중...</div>
                      <div style="display: flex;">
                          <div id="myPageNickname" style="color:green">닉네임 로딩 중</div>
                          <div id="myPageTag" style="color:green; margin-left: 2px;"></div>
                      </div>
                      <div id="myPageBio" style="margin-top: 5px; color: #555;">자기소개 로딩 중...</div>
                  </div>
              </div>
              <div style="display: flex; justify-content: flex-end; margin:5px 5px 0 0; ">
                  <button class="button_pro" style="margin-right:5px;" onclick="document.querySelector('.modal3').close(); window.location.href='/front/profile.html';">프로필 수정</button>
                  <button class="button_pro" onclick="logout()">로그아웃</button>
              </div>
          </div>  
        </dialog>`,document.body.appendChild(e),t=document.querySelector(".modal3"),t?a(t):console.error("Failed to create or find .modal3 for mypage.")}return t}async function b(){const t=document.getElementById("myPageDisplayId"),e=document.getElementById("myPageNickname"),d=document.getElementById("myPageTag"),o=document.getElementById("myPageBio"),n=document.getElementById("myPageProfileImage");if(!t||!e||!d||!o||!n){console.error("MyPage modal elements not found. Ensure createMyPageModalIfNeeded ran successfully.");return}try{const u=await fetch("/api/users/me");if(!u.ok){if(u.status===401){t.textContent="로그인 필요",e.textContent="",d.textContent="",o.textContent="로그인 후 이용해주세요.";return}throw new Error(`Failed to fetch user info: ${u.status}`)}const m=await u.json(),s=m.data||m;s?(s.loginId&&s.loginId.trim()!==""?t.textContent=s.loginId:t.textContent="소셜 로그인",e.textContent=s.nickname||"닉네임 정보 없음",d.textContent=s.identificationCode?`#${s.identificationCode}`:"",o.textContent=s.bio||"자기소개가 없습니다.",n.src=s.profileImage||"/icon/user2.png"):(console.error("User info data is not in expected format:",m),t.textContent="정보 형식 오류",e.textContent="",d.textContent="")}catch(u){console.error("Error fetching user info:",u),t.textContent="정보 로드 오류",e.textContent="",d.textContent="",o.textContent="정보를 불러오는 데 실패했습니다."}}async function x(){let t=g();t?(await b(),t.showModal()):console.error("Mypage modal (.modal3) could not be initialized for showing.")}function w(){window.location.href="/front/projectlist.html"}function C(){return r("div",{className:"root"},r("button",{className:"left1 drag1 clear",onClick:w},r("img",{src:"/icon/logo.png",className:"logo",style:{height:"50px"}})),r("button",{className:"right3 drag1 clear",onClick:x,id:"mypage"},r("i",{className:"fa-solid fa-user fa-2x",style:{fontSize:"25px"}})),r("button",{className:"right2 drag1 clear",onClick:()=>{let t=document.querySelector(".modal2");t||(l(),t=document.querySelector(".modal2")),t&&t.showModal()},id:"friend"},r("i",{className:"fa-solid fa-user-group fa-2x",style:{fontSize:"25px"}})),r("button",{className:"right1 drag1 clear",onClick:()=>{let t=document.querySelector(".modal1");t||(i(),t=document.querySelector(".modal1")),t&&t.showModal()},id:"alert"},r("i",{className:"fa-solid fa-bell fa-2x",style:{fontSize:"25px"}})))}f(document.getElementById("root")).render(r(C));function v(){document.querySelector(".modal1")||i(),document.querySelector(".modal2")||l(),g()}window.addEventListener("load",v);let p=document.querySelector("#root");if(p){let t=new MutationObserver(v),e={attributes:!0,childList:!0,characterData:!0};t.observe(p,e)}document.querySelectorAll(".lenCut_container").forEach(t=>{const e=t.querySelector(".lenCut"),d=t.querySelector(".lenCutE"),o=t.querySelector(".tooltip1");if(e){const n=e.textContent;n.length>6&&(e.textContent=n.slice(0,6)+"...",o&&(e.addEventListener("mouseenter",function(){o.style.display="block",o.textContent=n}),e.addEventListener("mouseleave",function(){o.style.display="none"})))}if(d){const n=d.textContent;n.length>15&&(d.textContent=n.slice(0,15)+"...",o&&(d.addEventListener("mouseenter",function(){o.style.display="block",o.textContent=n}),d.addEventListener("mouseleave",function(){o.style.display="none"})))}}),document.querySelectorAll(".button_select").forEach(t=>{let e=!1;t.addEventListener("click",()=>{e=!e,t.classList.toggle("active"),e?t.textContent="취소":t.textContent="선택"}),t.addEventListener("mouseenter",()=>{e&&(t.textContent="취소")}),t.addEventListener("mouseleave",()=>{e&&(t.textContent="선택")})})})();
