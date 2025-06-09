(function(){const p=document.createElement("link").relList;if(p&&p.supports&&p.supports("modulepreload"))return;for(const s of document.querySelectorAll('link[rel="modulepreload"]'))c(s);new MutationObserver(s=>{for(const l of s)if(l.type==="childList")for(const u of l.addedNodes)u.tagName==="LINK"&&u.rel==="modulepreload"&&c(u)}).observe(document,{childList:!0,subtree:!0});function g(s){const l={};return s.integrity&&(l.integrity=s.integrity),s.referrerPolicy&&(l.referrerPolicy=s.referrerPolicy),s.crossOrigin==="use-credentials"?l.credentials="include":s.crossOrigin==="anonymous"?l.credentials="omit":l.credentials="same-origin",l}function c(s){if(s.ep)return;s.ep=!0;const l=g(s);fetch(s.href,l)}})();(function(){const{createElement:a,Fragment:p}=React,{createRoot:g}=ReactDOM;function c(e){e.addEventListener("click",function(t){t.target===e&&e.close()})}window.closeModal=c;function s(){let e=document.querySelector(".modal1");if(!e){const n=document.createElement("div");n.innerHTML=`
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
        </dialog>`,document.body.appendChild(n),e=document.querySelector(".modal1"),e&&c(e)}const t=document.querySelector("#alertr"),o=()=>{const n=t.getBoundingClientRect(),i=e.getBoundingClientRect();e.style.position="fixed",e.style.left=`${n.right-i.width}px`,e.style.top=`${n.bottom}px`};t.addEventListener("click",()=>{e.showModal(),o()}),window.addEventListener("resize",()=>{e.open&&o()})}function l(){let e=document.querySelector(".modal2");if(!e){const o=document.createElement("div");o.innerHTML=`
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
        </dialog>`,document.body.appendChild(o),e=document.querySelector(".modal2"),setTimeout(()=>{u("friend1"),y()},0),document.body.style.overflow="hidden",e&&c(e);const n=document.getElementById("friendSearchBtn"),i=document.getElementById("friendSearchQuery");n&&i&&(n.onclick=()=>v(i.value.trim()),i.onkeypress=r=>{r.key==="Enter"&&v(i.value.trim())})}const t=document.querySelector("#friend");t&&t.addEventListener("click",()=>{let o=document.querySelector(".modal2");o||(l(),o=document.querySelector(".modal2")),o&&(o.showModal(),u("friend1"))}),e&&e.addEventListener("close",()=>{document.body.style.overflow="auto"})}function u(e){if(["friend1","friend2","friend3"].forEach(n=>{const i=document.getElementById(n);i&&(i.style.display=n===e?"block":"none")}),document.querySelectorAll(".button_friend").forEach((n,i)=>{"friend"+(i+1).toString()===e?n.classList.add("active"):n.classList.remove("active")}),e==="friend1")y();else if(e==="friend2"){const n=document.getElementById("userSearchResultTbody");n&&(n.innerHTML='<tr><td colspan="5" style="text-align:center; color:grey;">닉네임으로 사용자를 검색하세요.</td></tr>')}else e==="friend3"&&x()}window.Watchdiv=u;async function v(e){const t=document.getElementById("userSearchResultTbody");if(t){if(!e){t.innerHTML='<tr><td colspan="5" style="text-align:center; color:grey;">검색어를 입력해주세요.</td></tr>';return}t.innerHTML='<tr><td colspan="5" style="text-align:center; color:grey;">검색 중...</td></tr>';try{const o=await fetch(`/api/friends/search?query=${encodeURIComponent(e)}`);if(!o.ok){const i=await o.json().catch(()=>({message:"검색 중 오류 발생"}));throw new Error(i.message||`Error: ${o.status}`)}const n=await o.json();n.status===200&&Array.isArray(n.data)?E(n.data):t.innerHTML=`<tr><td colspan="5" style="text-align:center; color:red;">검색 결과를 가져오지 못했습니다: ${n.message}</td></tr>`}catch(o){console.error("Error searching users:",o),t.innerHTML=`<tr><td colspan="5" style="text-align:center; color:red;">검색 중 오류: ${o.message}</td></tr>`}}}function E(e){const t=document.getElementById("userSearchResultTbody");if(t.innerHTML="",e.length===0){t.innerHTML='<tr><td colspan="5" style="text-align:center; color:grey;">검색 결과가 없습니다.</td></tr>';return}e.forEach(o=>{const n=document.createElement("tr");n.innerHTML=`
                <td><img src="${o.profileImage||"/icon/user2.png"}" style="height: 30px; width: 30px; margin:5px; border-radius:50%; object-fit:cover;"/></td>
                <td class="lenCut_container"><span class="lenCut" title="${o.nickname}">${o.nickname}</span><div class="tooltip1"></div></td>
                <td><div class="lenCut_container"><span class="lenCutE" style="font-size: small; color: rgba(0, 0, 0, 0.5);" title="${o.email||""}">${o.email||"-"}</span><div class="tooltip1"></div></div></td>
                <td><span style="color: #3a6b5b;">#${o.identificationCode}</span></td>
                <td id="actionCell_${o.userIdx}"></td>
            `,t.appendChild(n),h(o.userIdx,o.friendStatus)}),m()}function h(e,t){const o=document.getElementById(`actionCell_${e}`);if(!o)return;o.innerHTML="";let n;switch(t){case"NONE":n=document.createElement("button"),n.className="button_select",n.style.width="80px",n.textContent="친구 신청",n.onclick=()=>T(e);break;case"PENDING_SENT":n=document.createElement("button"),n.className="button_sent",n.style.width="80px",n.textContent="요청 보냄",n.disabled=!0;break;case"PENDING_RECEIVED":o.innerHTML='<span style="font-size:small; color:orange;">요청 받음</span>';return;case"ACCEPTED":o.innerHTML='<span style="font-size:small; color:green;">친구</span>';return;case"SELF":o.innerHTML='<span style="font-size:small; color:grey;">본인</span>';return;default:o.innerHTML="-";return}n&&o.appendChild(n)}async function T(e){if(confirm("친구 요청을 보내시겠습니까?"))try{const t=await fetch("/api/friends/request",{method:"POST",headers:{"Content-Type":"application/json"},body:JSON.stringify({recipientUserId:e})}),o=await t.json();if(t.status===201)alert("친구 요청을 보냈습니다."),h(e,"PENDING_SENT");else throw new Error(o.message||"친구 요청 실패")}catch(t){console.error("Error sending friend request:",t),alert(`오류: ${t.message}`)}}async function y(){const e=document.getElementById("friendListTbody");if(e){e.innerHTML='<tr><td colspan="5" style="text-align:center; color:grey;">친구 목록 로딩 중...</td></tr>';try{const t=await fetch("/api/friends");if(!t.ok)throw new Error(`Error: ${t.status}`);const o=await t.json();if(e.innerHTML="",o.status===200&&Array.isArray(o.data)){if(o.data.length===0){e.innerHTML='<tr><td colspan="5" style="text-align:center; color:grey;">친구가 없습니다.</td></tr>';return}o.data.forEach(n=>{const i=document.createElement("tr");i.innerHTML=`
                        <td><img src="${n.profileImage||"/icon/user2.png"}" style="height: 30px; width: 30px; margin:5px; border-radius:50%; object-fit:cover;"/></td>
                        <td class="lenCut_container"><span class="lenCut" title="${n.nickname}">${n.nickname}</span><div class="tooltip1"></div></td>
                        <td><div class="lenCut_container"><span class="lenCutE" style="font-size: small; color: rgba(0, 0, 0, 0.5);" title="${n.email||""}">${n.email||"-"}</span><div class="tooltip1"></div></div></td>
                        <td><span style="color: #3a6b5b;">#${n.identificationCode}</span></td>
                        <td><button class="button_no" style="width:auto; padding:0 5px; height:25px; font-size:13px;" onclick="removeFriendHandler(${n.userIdx}, '${n.nickname}')">친구 삭제</button></td>
                    `,e.appendChild(i)}),m()}else e.innerHTML=`<tr><td colspan="5" style="text-align:center; color:red;">친구 목록 로드 실패: ${o.message}</td></tr>`}catch(t){console.error("Error loading friend list:",t),e.innerHTML='<tr><td colspan="5" style="text-align:center; color:red;">친구 목록 로드 오류.</td></tr>'}}}async function L(e,t){if(confirm(`'${t}'님을 친구 목록에서 삭제하시겠습니까?`))try{const o=await fetch(`/api/friends/${e}`,{method:"DELETE"});if(o.ok){alert("친구를 삭제했습니다."),y();const n=document.querySelector(`#actionCell_${e} button`);n&&n.textContent!=="친구 신청"&&h(e,"NONE")}else{const n=await o.json().catch(()=>({message:"친구 삭제 실패"}));throw new Error(n.message)}}catch(o){console.error("Error removing friend:",o),alert(`오류: ${o.message}`)}}window.removeFriendHandler=L;async function x(){const e=document.getElementById("receivedRequestsTbody");if(e){e.innerHTML='<tr><td colspan="5" style="text-align:center; color:grey;">받은 친구 요청 로딩 중...</td></tr>';try{const t=await fetch("/api/friends/requests/received");if(!t.ok)throw new Error(`Error: ${t.status}`);const o=await t.json();if(e.innerHTML="",o.status===200&&Array.isArray(o.data)){if(o.data.length===0){e.innerHTML='<tr><td colspan="5" style="text-align:center; color:grey;">받은 친구 요청이 없습니다.</td></tr>';return}o.data.forEach(n=>{const i=n.user,r=document.createElement("tr");r.innerHTML=`
                        <td><img src="${i.profileImage||"/icon/user2.png"}" style="height: 30px; width: 30px; margin:5px; border-radius:50%; object-fit:cover;"/></td>
                        <td class="lenCut_container"><span class="lenCut" title="${i.nickname}">${i.nickname}</span><div class="tooltip1"></div></td>
                        <td><div class="lenCut_container"><span class="lenCutE" style="font-size: small; color: rgba(0, 0, 0, 0.5);" title="${i.email||""}">${i.email||"-"}</span><div class="tooltip1"></div></div></td>
                        <td><span style="color: #3a6b5b;">#${i.identificationCode}</span></td>
                        <td>
                            <div style="display: flex; flex-direction: row; gap: 3px;">
                                <button class="button_yes" onclick="acceptFriendRequestHandler(${i.userIdx})">수락</button>
                                <button class="button_no" onclick="rejectFriendRequestHandler(${i.userIdx})">거절</button>
                            </div>
                        </td>
                    `,e.appendChild(r)}),m()}else e.innerHTML=`<tr><td colspan="5" style="text-align:center; color:red;">받은 요청 목록 로드 실패: ${o.message}</td></tr>`}catch(t){console.error("Error loading received requests:",t),e.innerHTML='<tr><td colspan="5" style="text-align:center; color:red;">받은 요청 목록 로드 오류.</td></tr>'}}}async function S(e){try{const t=await fetch(`/api/friends/accept/${e}`,{method:"PATCH"}),o=await t.json();if(t.ok)alert("친구 요청을 수락했습니다."),x(),y();else throw new Error(o.message||"친구 요청 수락 실패")}catch(t){console.error("Error accepting friend request:",t),alert(`오류: ${t.message}`)}}window.acceptFriendRequestHandler=S;async function M(e){try{const t=await fetch(`/api/friends/reject/${e}`,{method:"PATCH"}),o=await t.json();if(t.ok)alert("친구 요청을 거절했습니다."),x();else throw new Error(o.message||"친구 요청 거절 실패")}catch(t){console.error("Error rejecting friend request:",t),alert(`오류: ${t.message}`)}}window.rejectFriendRequestHandler=M;function m(){document.querySelectorAll(".lenCut_container").forEach(e=>{const t=e.querySelector(".lenCut"),o=e.querySelector(".lenCutE"),n=e.querySelector(".tooltip1"),i=(r,f)=>{if(r&&n){const d=r.dataset.originalText||r.textContent;r.dataset.originalText=d,d.length>f?r.textContent=d.slice(0,f)+"...":r.textContent=d,r.onmouseenter=function(){(r.offsetWidth<r.scrollWidth||r.textContent.endsWith("..."))&&(n.style.display="block",n.textContent=d)},r.onmouseleave=function(){n.style.display="none"}}};i(t,6),i(o,15)})}window.addEventListener("load",m);function k(){confirm("로그아웃 하겠습니까?")&&fetch("/api/auth/logout",{method:"POST"}).then(t=>{t.ok?window.location.href="/front/login.html?logout":alert("로그아웃에 실패했습니다. 다시 시도해주세요.")}).catch(t=>{console.error("Logout error:",t),alert("로그아웃 중 오류가 발생했습니다.")})}window.logout=k;function b(){let e=document.querySelector(".modal3");if(!e){const n=document.createElement("div");n.innerHTML=`
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
      </dialog>`,document.body.appendChild(n),e=document.querySelector(".modal3"),e?c(e):console.error("Failed to create or find .modal3 for mypage.")}const t=document.querySelector("#mypage"),o=()=>{const n=t.getBoundingClientRect(),i=e.getBoundingClientRect();e.style.position="fixed",e.style.left=`${n.right-i.width}px`,e.style.top=`${n.bottom}px`};return t.addEventListener("click",()=>{e.showModal(),o()}),window.addEventListener("resize",()=>{e.open&&o()}),e}async function $(){const e=document.getElementById("myPageDisplayId"),t=document.getElementById("myPageNickname"),o=document.getElementById("myPageTag"),n=document.getElementById("myPageBio"),i=document.getElementById("myPageProfileImage");if(!e||!t||!o||!n||!i){console.error("MyPage modal elements not found. Ensure createMyPageModalIfNeeded ran successfully.");return}try{const r=await fetch("/api/users/me");if(!r.ok){if(r.status===401){e.textContent="로그인 필요",t.textContent="",o.textContent="",n.textContent="로그인 후 이용해주세요.";return}throw new Error(`Failed to fetch user info: ${r.status}`)}const f=await r.json(),d=f.data||f;d?(d.loginId&&d.loginId.trim()!==""?e.textContent=d.loginId:e.textContent="소셜 로그인",t.textContent=d.nickname||"닉네임 정보 없음",o.textContent=d.identificationCode?`#${d.identificationCode}`:"",n.textContent=d.bio||"자기소개가 없습니다.",i.src=d.profileImage||"/icon/user2.png"):(console.error("User info data is not in expected format:",f),e.textContent="정보 형식 오류",t.textContent="",o.textContent="")}catch(r){console.error("Error fetching user info:",r),e.textContent="정보 로드 오류",t.textContent="",o.textContent="",n.textContent="정보를 불러오는 데 실패했습니다."}}async function q(){let e=b();e?(await $(),e.showModal()):console.error("Mypage modal (.modal3) could not be initialized for showing.")}function I(){window.location.href="/front/projectlist.html"}function H(){return a("div",{className:"root"},a("button",{className:"left1 drag1 clear",onClick:I},a("img",{src:"/icon/logo.png",className:"logo",style:{height:"50px"}})),a("button",{className:"right3 drag1 clear",onClick:q,id:"mypage"},a("i",{className:"fa-solid fa-user fa-2x",style:{fontSize:"25px"}})),a("button",{className:"right2 drag1 clear",onClick:()=>{let e=document.querySelector(".modal2");e||(l(),e=document.querySelector(".modal2")),e&&e.showModal()},id:"friend"},a("i",{className:"fa-solid fa-user-group fa-2x",style:{fontSize:"25px"}})),a("button",{className:"right1 drag1 clear",onClick:s,id:"alertr"},a("i",{className:"fa-solid fa-bell fa-2x",style:{fontSize:"25px"}})))}g(document.getElementById("root")).render(a(H));function w(){document.querySelector(".modal1")||s(),document.querySelector(".modal2")||l(),b()}window.addEventListener("load",w);let C=document.querySelector("#root");if(C){let e=new MutationObserver(w),t={attributes:!0,childList:!0,characterData:!0};e.observe(C,t)}document.querySelectorAll(".lenCut_container").forEach(e=>{const t=e.querySelector(".lenCut"),o=e.querySelector(".lenCutE"),n=e.querySelector(".tooltip1");if(t){const i=t.textContent;i.length>6&&(t.textContent=i.slice(0,6)+"...",n&&(t.addEventListener("mouseenter",function(){n.style.display="block",n.textContent=i}),t.addEventListener("mouseleave",function(){n.style.display="none"})))}if(o){const i=o.textContent;i.length>15&&(o.textContent=i.slice(0,15)+"...",n&&(o.addEventListener("mouseenter",function(){n.style.display="block",n.textContent=i}),o.addEventListener("mouseleave",function(){n.style.display="none"})))}}),document.querySelectorAll(".button_select").forEach(e=>{let t=!1;e.addEventListener("click",()=>{t=!t,e.classList.toggle("active"),t?e.textContent="취소":e.textContent="선택"}),e.addEventListener("mouseenter",()=>{t&&(e.textContent="취소")}),e.addEventListener("mouseleave",()=>{t&&(e.textContent="선택")})})})();
