(function(){const u=document.createElement("link").relList;if(u&&u.supports&&u.supports("modulepreload"))return;for(const l of document.querySelectorAll('link[rel="modulepreload"]'))r(l);new MutationObserver(l=>{for(const n of l)if(n.type==="childList")for(const c of n.addedNodes)c.tagName==="LINK"&&c.rel==="modulepreload"&&r(c)}).observe(document,{childList:!0,subtree:!0});function y(l){const n={};return l.integrity&&(n.integrity=l.integrity),l.referrerPolicy&&(n.referrerPolicy=l.referrerPolicy),l.crossOrigin==="use-credentials"?n.credentials="include":l.crossOrigin==="anonymous"?n.credentials="omit":n.credentials="same-origin",n}function r(l){if(l.ep)return;l.ep=!0;const n=y(l);fetch(l.href,n)}})();(function(){const{createElement:s,Fragment:u}=React,{createRoot:y}=ReactDOM;function r(t){t.addEventListener("click",function(e){e.target===t&&t.close()})}window.closeModal=r;function l(){let t=document.querySelector(".modal1");if(!t){const i=document.createElement("div");i.innerHTML=`<dialog class="modal1">
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
            </dialog>`,document.body.appendChild(i),t=document.querySelector(".modal1"),t&&r(t)}const e=document.querySelector("#alertr"),o=()=>{const i=e.getBoundingClientRect(),d=t.getBoundingClientRect();t.style.position="fixed",t.style.left=`${i.right-d.width}px`,t.style.top=`${i.bottom}px`};e.addEventListener("click",()=>{currentModal.showModal(),requestAnimationFrame(()=>requestAnimationFrame(o))}),window.addEventListener("resize",()=>{t.open&&o()})}function n(){let t=document.querySelector(".modal2");if(!t){const o=document.createElement("div");o.innerHTML=`
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
          </dialog>`,document.body.appendChild(o),t=document.querySelector(".modal2"),setTimeout(()=>{c("friend1")},0),t&&r(t)}const e=document.querySelector("#friend");e&&e.addEventListener("click",()=>{const o=document.querySelector(".modal2");o&&(o.showModal(),setTimeout(()=>{c("friend1")},0))})}function c(t){["friend1","friend2","friend3"].forEach(i=>{const d=document.getElementById(i);d&&(d.style.display=i===t?"block":"none")}),document.querySelectorAll(".button_friend").forEach((i,d)=>{"friend"+(d+1).toString()===t?i.classList.add("active"):i.classList.remove("active")})}window.Watchdiv=c;function h(){confirm("로그아웃 하겠습니까?")&&fetch("/api/auth/logout",{method:"POST"}).then(e=>{e.ok?window.location.href="/front/login.html?logout":alert("로그아웃에 실패했습니다. 다시 시도해주세요.")}).catch(e=>{console.error("Logout error:",e),alert("로그아웃 중 오류가 발생했습니다.")})}window.logout=h;function v(){let t=document.querySelector(".modal3");if(!t){const i=document.createElement("div");i.innerHTML=`
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
      </dialog>`,document.body.appendChild(i),t=document.querySelector(".modal3"),t?r(t):console.error("Failed to create or find .modal3 for mypage.")}const e=document.querySelector("#mypage"),o=()=>{const i=e.getBoundingClientRect(),d=t.getBoundingClientRect();t.style.position="fixed",t.style.left=`${i.right-d.width}px`,t.style.top=`${i.bottom}px`};return e.addEventListener("click",()=>{t.showModal(),o()}),window.addEventListener("resize",()=>{t.open&&o()}),t}async function x(){const t=document.getElementById("myPageDisplayId"),e=document.getElementById("myPageNickname"),o=document.getElementById("myPageTag"),i=document.getElementById("myPageBio"),d=document.getElementById("myPageProfileImage");if(!t||!e||!o||!i||!d){console.error("MyPage modal elements not found. Ensure createMyPageModalIfNeeded ran successfully.");return}try{const p=await fetch("/api/users/me");if(!p.ok){if(p.status===401){t.textContent="로그인 필요",e.textContent="",o.textContent="",i.textContent="로그인 후 이용해주세요.";return}throw new Error(`Failed to fetch user info: ${p.status}`)}const f=await p.json(),a=f.data||f;a?(a.loginId&&a.loginId.trim()!==""?t.textContent=a.loginId:t.textContent="소셜 로그인",e.textContent=a.nickname||"닉네임 정보 없음",o.textContent=a.identificationCode?`#${a.identificationCode}`:"",i.textContent=a.bio||"자기소개가 없습니다.",d.src=a.profileImage||"/icon/user2.png"):(console.error("User info data is not in expected format:",f),t.textContent="정보 형식 오류",e.textContent="",o.textContent="")}catch(p){console.error("Error fetching user info:",p),t.textContent="정보 로드 오류",e.textContent="",o.textContent="",i.textContent="정보를 불러오는 데 실패했습니다."}}async function b(){let t=v();t?(await x(),t.showModal()):console.error("Mypage modal (.modal3) could not be initialized for showing.")}function w(){window.location.href="/front/projectlist.html"}function C(){return s("div",{className:"root"},s("button",{className:"left1 drag1 clear",onClick:w},s("img",{src:"/icon/logo.png",className:"logo",style:{height:"50px"}})),s("button",{className:"right3 drag1 clear",onClick:b,id:"mypage"},s("i",{className:"fa-solid fa-user fa-2x",style:{fontSize:"25px"}})),s("button",{className:"right2 drag1 clear",onClick:()=>{let t=document.querySelector(".modal2");t||(n(),t=document.querySelector(".modal2")),t&&t.showModal()},id:"friend"},s("i",{className:"fa-solid fa-user-group fa-2x",style:{fontSize:"25px"}})),s("button",{className:"right1 drag1 clear",onClick:()=>{let t=document.querySelector(".modal1");t||(l(),t=document.querySelector(".modal1")),t&&t.showModal()},id:"alertr"},s("i",{className:"fa-solid fa-bell fa-2x",style:{fontSize:"25px"}})))}y(document.getElementById("root")).render(s(C));function m(){document.querySelector(".modal1")||l(),document.querySelector(".modal2")||n(),v()}window.addEventListener("load",m);let g=document.querySelector("#root");if(g){let t=new MutationObserver(m),e={attributes:!0,childList:!0,characterData:!0};t.observe(g,e)}document.querySelectorAll(".lenCut_container").forEach(t=>{const e=t.querySelector(".lenCut"),o=t.querySelector(".lenCutE"),i=t.querySelector(".tooltip1");if(e){const d=e.textContent;d.length>6&&(e.textContent=d.slice(0,6)+"...",i&&(e.addEventListener("mouseenter",function(){i.style.display="block",i.textContent=d}),e.addEventListener("mouseleave",function(){i.style.display="none"})))}if(o){const d=o.textContent;d.length>15&&(o.textContent=d.slice(0,15)+"...",i&&(o.addEventListener("mouseenter",function(){i.style.display="block",i.textContent=d}),o.addEventListener("mouseleave",function(){i.style.display="none"})))}}),document.querySelectorAll(".button_select").forEach(t=>{let e=!1;t.addEventListener("click",()=>{e=!e,t.classList.toggle("active"),e?t.textContent="취소":t.textContent="선택"}),t.addEventListener("mouseenter",()=>{e&&(t.textContent="취소")}),t.addEventListener("mouseleave",()=>{e&&(t.textContent="선택")})})})();
