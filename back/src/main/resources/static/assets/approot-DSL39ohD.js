(function(){const u=document.createElement("link").relList;if(u&&u.supports&&u.supports("modulepreload"))return;for(const l of document.querySelectorAll('link[rel="modulepreload"]'))r(l);new MutationObserver(l=>{for(const s of l)if(s.type==="childList")for(const c of s.addedNodes)c.tagName==="LINK"&&c.rel==="modulepreload"&&r(c)}).observe(document,{childList:!0,subtree:!0});function f(l){const s={};return l.integrity&&(s.integrity=l.integrity),l.referrerPolicy&&(s.referrerPolicy=l.referrerPolicy),l.crossOrigin==="use-credentials"?s.credentials="include":l.crossOrigin==="anonymous"?s.credentials="omit":s.credentials="same-origin",s}function r(l){if(l.ep)return;l.ep=!0;const s=f(l);fetch(l.href,s)}})();(function(){const{createElement:d,Fragment:u}=React,{createRoot:f}=ReactDOM;function r(t){t.addEventListener("click",function(e){e.target===t&&t.close()})}window.closeModal=r;function l(){let t=document.querySelector(".modal1");if(!t){const i=document.createElement("div");i.innerHTML=`
        <dialog class="modal1">
        <div class="wh100" style="display:flex; flex-direction: column;">
            <div style="padding: 10px; font-size: large; font-weight: bold;">알림</div>
            
            <hr style="margin: 0;">            
            <div class="box_scroll">
            <table class="table table-hover" style="overflow-y:auto;">
                <tbody id="tbody_alert">
                    <tr><td>
                        <div style="display: flex; flex-direction: column; padding: 5px;">
                            <span>알림 내용</span>
                            <br/>
                            <div style="display: flex; flex-direction: row;">
                                <span style="font-size: small;">2025/00/00 00:00</span>
                                <img src="/icon/trash.png" style="height: 20px; width: 20px; margin-left: auto;"/>                                    
                            </div>
                        </div>
                    </td></tr>
                    <tr><td>
                        <div style="display: flex; flex-direction: column; padding: 5px;">
                            <div style="display: flex; flex-direction: row;">
                                <span>요청 알림 내용</span>
                                <div style="margin-left: auto; margin-right: 20px;">
                                    <button class="button_yes">수락</button>
                                    <button class="button_no">거부</button>                                      
                                </div>        
                            </div>
                            <br/>
                            <div style="display: flex; flex-direction: row;">
                                <span style="font-size: small;">2025/00/00 00:00</span>
                                <img src="/icon/trash.png" style="height: 20px; width: 20px; margin-left: auto;"/>                                    
                            </div>
                        </div>
                    </td></tr>
                    <tr><td>
                        <div style="display: flex; flex-direction: column; padding: 5px;">
                            <span>스크롤 확인용</span>
                            <br/>
                            <div style="display: flex; flex-direction: row;">
                                <span style="font-size: small;">2025/00/00 00:00</span>
                                <img src="/icon/trash.png" style="height: 20px; width: 20px; margin-left: auto;"/>                                    
                            </div>
                        </div>
                    </td></tr>
                    <tr><td>
                        <div style="display: flex; flex-direction: column; padding: 5px;">
                            <span>스크롤 확인용</span>
                            <br/>
                            <div style="display: flex; flex-direction: row;">
                                <span style="font-size: small;">2025/00/00 00:00</span>
                                <img src="/icon/trash.png" style="height: 20px; width: 20px; margin-left: auto;"/>                                    
                            </div>
                        </div>
                    </td></tr>
                    <tr><td>
                        <div style="display: flex; flex-direction: column; padding: 5px;">
                            <span>스크롤 확인용</span>
                            <br/>
                            <div style="display: flex; flex-direction: row;">
                                <span style="font-size: small;">2025/00/00 00:00</span>
                                <img src="/icon/trash.png" style="height: 20px; width: 20px; margin-left: auto;"/>                                    
                            </div>
                        </div>
                    </td></tr>
                    <tr><td>
                        <div style="display: flex; flex-direction: column; padding: 5px;">
                            <span>스크롤 확인용</span>
                            <br/>
                            <div style="display: flex; flex-direction: row;">
                                <span style="font-size: small;">2025/00/00 00:00</span>
                                <img src="/icon/trash.png" style="height: 20px; width: 20px; margin-left: auto;"/>                                    
                            </div>
                        </div>
                    </td></tr>
                    <tr><td>
                        <div style="display: flex; flex-direction: column; padding: 5px;">
                            <span>스크롤 확인용</span>
                            <br/>
                            <div style="display: flex; flex-direction: row;">
                                <span style="font-size: small;">2025/00/00 00:00</span>
                                <img src="/icon/trash.png" style="height: 20px; width: 20px; margin-left: auto;"/>                                    
                            </div>
                        </div>
                    </td></tr>

                </tbody>
            </table>
            </div>
        <div>
        </dialog>`,document.body.appendChild(i),t=document.querySelector(".modal1"),t&&r(t)}const e=document.querySelector("#alertr"),o=()=>{const i=e.getBoundingClientRect(),n=t.getBoundingClientRect();t.style.position="fixed",t.style.left=`${i.right-n.width}px`,t.style.top=`${i.bottom}px`};e.addEventListener("click",()=>{t.showModal(),o()}),window.addEventListener("resize",()=>{t.open&&o()})}function s(){let t=document.querySelector(".modal2");if(!t){const o=document.createElement("div");o.innerHTML=`
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
                                        <img src="/icon/user2.png" style="height: 30px; width: 30px; margin:5px;"/>
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
                                        <img src="/icon/user2.png" style="height: 30px; width: 30px; margin:5px;"/>
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
                
                    <div class="find3">
                        <input class="button_find3" type="text" id="find" placeholder="아이디 / 태그로 유저 검색" style="font-size: small; width: 400px;"/> 
                        <img src="/icon/finding.png" style="width: 25px; height: 25px; margin: 5px; margin-left: auto;"/>                    
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
                                        <img src="/icon/user2.png" style="height: 30px; width: 30px; margin:5px;"/>
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
                                        <img src="/icon/user2.png" style="height: 30px; width: 30px; margin:5px;"/>
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
                                      <img src="/icon/user2.png" style="height: 30px; width: 30px; margin:5px;"/>
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
          </dialog>`,document.body.appendChild(o),t=document.querySelector(".modal2"),setTimeout(()=>{c("friend1")},0),t&&r(t)}const e=document.querySelector("#friend");e&&e.addEventListener("click",()=>{const o=document.querySelector(".modal2");o&&(o.showModal(),setTimeout(()=>{c("friend1")},0))})}function c(t){["friend1","friend2","friend3"].forEach(i=>{const n=document.getElementById(i);n&&(n.style.display=i===t?"block":"none")}),document.querySelectorAll(".button_friend").forEach((i,n)=>{"friend"+(n+1).toString()===t?i.classList.add("active"):i.classList.remove("active")})}window.Watchdiv=c;function x(){confirm("로그아웃 하겠습니까?")&&fetch("/api/auth/logout",{method:"POST"}).then(e=>{e.ok?window.location.href="/front/login.html?logout":alert("로그아웃에 실패했습니다. 다시 시도해주세요.")}).catch(e=>{console.error("Logout error:",e),alert("로그아웃 중 오류가 발생했습니다.")})}window.logout=x;function m(){let t=document.querySelector(".modal3");if(!t){const i=document.createElement("div");i.innerHTML=`
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
      </dialog>`,document.body.appendChild(i),t=document.querySelector(".modal3"),t?r(t):console.error("Failed to create or find .modal3 for mypage.")}const e=document.querySelector("#mypage"),o=()=>{const i=e.getBoundingClientRect(),n=t.getBoundingClientRect();t.style.position="fixed",t.style.left=`${i.right-n.width}px`,t.style.top=`${i.bottom}px`};return e.addEventListener("click",()=>{t.showModal(),o()}),window.addEventListener("resize",()=>{t.open&&o()}),t}async function v(){const t=document.getElementById("myPageDisplayId"),e=document.getElementById("myPageNickname"),o=document.getElementById("myPageTag"),i=document.getElementById("myPageBio"),n=document.getElementById("myPageProfileImage");if(!t||!e||!o||!i||!n){console.error("MyPage modal elements not found. Ensure createMyPageModalIfNeeded ran successfully.");return}try{const p=await fetch("/api/users/me");if(!p.ok){if(p.status===401){t.textContent="로그인 필요",e.textContent="",o.textContent="",i.textContent="로그인 후 이용해주세요.";return}throw new Error(`Failed to fetch user info: ${p.status}`)}const y=await p.json(),a=y.data||y;a?(a.loginId&&a.loginId.trim()!==""?t.textContent=a.loginId:t.textContent="소셜 로그인",e.textContent=a.nickname||"닉네임 정보 없음",o.textContent=a.identificationCode?`#${a.identificationCode}`:"",i.textContent=a.bio||"자기소개가 없습니다.",n.src=a.profileImage||"/icon/user2.png"):(console.error("User info data is not in expected format:",y),t.textContent="정보 형식 오류",e.textContent="",o.textContent="")}catch(p){console.error("Error fetching user info:",p),t.textContent="정보 로드 오류",e.textContent="",o.textContent="",i.textContent="정보를 불러오는 데 실패했습니다."}}async function b(){let t=m();t?(await v(),t.showModal()):console.error("Mypage modal (.modal3) could not be initialized for showing.")}function w(){window.location.href="/front/projectlist.html"}function C(){return d("div",{className:"root"},d("button",{className:"left1 drag1 clear",onClick:w},d("img",{src:"/icon/logo.png",className:"logo",style:{height:"50px"}})),d("button",{className:"right3 drag1 clear",onClick:b,id:"mypage"},d("i",{className:"fa-solid fa-user fa-2x",style:{fontSize:"25px"}})),d("button",{className:"right2 drag1 clear",onClick:()=>{let t=document.querySelector(".modal2");t||(s(),t=document.querySelector(".modal2")),t&&t.showModal()},id:"friend"},d("i",{className:"fa-solid fa-user-group fa-2x",style:{fontSize:"25px"}})),d("button",{className:"right1 drag1 clear",onClick:l,id:"alertr"},d("i",{className:"fa-solid fa-bell fa-2x",style:{fontSize:"25px"}})))}f(document.getElementById("root")).render(d(C));function g(){document.querySelector(".modal1")||l(),document.querySelector(".modal2")||s(),m()}window.addEventListener("load",g);let h=document.querySelector("#root");if(h){let t=new MutationObserver(g),e={attributes:!0,childList:!0,characterData:!0};t.observe(h,e)}document.querySelectorAll(".lenCut_container").forEach(t=>{const e=t.querySelector(".lenCut"),o=t.querySelector(".lenCutE"),i=t.querySelector(".tooltip1");if(e){const n=e.textContent;n.length>6&&(e.textContent=n.slice(0,6)+"...",i&&(e.addEventListener("mouseenter",function(){i.style.display="block",i.textContent=n}),e.addEventListener("mouseleave",function(){i.style.display="none"})))}if(o){const n=o.textContent;n.length>15&&(o.textContent=n.slice(0,15)+"...",i&&(o.addEventListener("mouseenter",function(){i.style.display="block",i.textContent=n}),o.addEventListener("mouseleave",function(){i.style.display="none"})))}}),document.querySelectorAll(".button_select").forEach(t=>{let e=!1;t.addEventListener("click",()=>{e=!e,t.classList.toggle("active"),e?t.textContent="취소":t.textContent="선택"}),t.addEventListener("mouseenter",()=>{e&&(t.textContent="취소")}),t.addEventListener("mouseleave",()=>{e&&(t.textContent="선택")})})})();
