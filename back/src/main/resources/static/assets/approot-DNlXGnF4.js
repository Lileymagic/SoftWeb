(function(){const p=document.createElement("link").relList;if(p&&p.supports&&p.supports("modulepreload"))return;for(const s of document.querySelectorAll('link[rel="modulepreload"]'))u(s);new MutationObserver(s=>{for(const a of s)if(a.type==="childList")for(const y of a.addedNodes)y.tagName==="LINK"&&y.rel==="modulepreload"&&u(y)}).observe(document,{childList:!0,subtree:!0});function g(s){const a={};return s.integrity&&(a.integrity=s.integrity),s.referrerPolicy&&(a.referrerPolicy=s.referrerPolicy),s.crossOrigin==="use-credentials"?a.credentials="include":s.crossOrigin==="anonymous"?a.credentials="omit":a.credentials="same-origin",a}function u(s){if(s.ep)return;s.ep=!0;const a=g(s);fetch(s.href,a)}})();(function(){const{createElement:d,Fragment:p}=React,{createRoot:g}=ReactDOM;function u(e){e.addEventListener("click",function(t){t.target===e&&e.close()})}window.closeModal=u;function s(){let e=document.querySelector(".modal1");if(!e){const o=document.createElement("div");o.innerHTML=`
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
          </dialog>`,document.body.appendChild(o),e=document.querySelector(".modal1"),e&&u(e)}const t=document.querySelector("#alertr"),n=()=>{const o=t.getBoundingClientRect(),r=e.getBoundingClientRect();e.style.position="fixed",e.style.left=`${o.right-r.width}px`,e.style.top=`${o.bottom}px`};t.addEventListener("click",()=>{e.showModal(),n(),a()}),window.addEventListener("resize",()=>{e.open&&n()})}async function a(){const e=document.getElementById("tbody_alert");if(e){e.innerHTML='<tr><td style="text-align:center; color:grey; padding:20px;">알림을 불러오는 중...</td></tr>';try{const t=await fetch("/api/notifications");if(!t.ok)throw new Error(`서버 오류: ${t.status}`);const n=await t.json();if(n.status===200&&Array.isArray(n.data)){if(e.innerHTML="",n.data.length===0){e.innerHTML='<tr><td style="text-align:center; color:grey; padding:20px;">새로운 알림이 없습니다.</td></tr>';return}n.data.slice(0,30).forEach(r=>{const i=document.createElement("tr");i.id=`notification-${r.idx}`,i.innerHTML=y(r),e.appendChild(i)})}else e.innerHTML=`<tr><td style="text-align:center; color:red; padding:20px;">알림을 불러오지 못했습니다: ${n.message}</td></tr>`}catch(t){console.error("알림 로딩 실패:",t),e.innerHTML='<tr><td style="text-align:center; color:red; padding:20px;">알림 로딩 중 오류가 발생했습니다.</td></tr>'}}}function y(e){const t=e.idx,n=e.referenceIdx,o=new Date(e.createdAt).toLocaleString();let r="";switch(e.type){case"FRIEND_REQUEST":r=`
                    <div style="display: flex; flex-direction: row; align-items: center;">
                        <span>${e.content}</span>
                        <div style="margin-left: auto; margin-right: 10px; display:flex; gap:5px;">
                            <button class="button_yes" onclick="handleFriendRequest(${n}, true, ${t})">수락</button>
                            <button class="button_no" onclick="handleFriendRequest(${n}, false, ${t})">거부</button>
                        </div>
                    </div>`;break;case"PROJECT_INVITED":r=`
                    <div style="display: flex; flex-direction: row; align-items: center;">
                        <span>${e.content}</span>
                        <div style="margin-left: auto; margin-right: 10px; display:flex; gap:5px;">
                            <button class="button_yes" onclick="handleProjectInvite(${n}, true, ${t})">수락</button>
                            <button class="button_no" onclick="handleProjectInvite(${n}, false, ${t})">거부</button>
                        </div>
                    </div>`;break;default:r=`<span>${e.content}</span>`;break}return`
            <td>
                <div style="display: flex; flex-direction: column; padding: 5px; gap: 8px;">
                    ${r}
                    <div style="display: flex; flex-direction: row; align-items: center;">
                        <span style="font-size: small; color: grey;">${o}</span>
                        <img src="/icon/trash.png" style="height: 20px; width: 20px; margin-left: auto; cursor:pointer;" onclick="deleteNotification(${t})"/>
                    </div>
                </div>
            </td>`}async function h(e){if(confirm("이 알림을 삭제하시겠습니까?"))try{const t=await fetch(`/api/notifications/${e}`,{method:"DELETE"});if(t.ok){const n=document.getElementById(`notification-${e}`);n&&n.remove();const o=document.getElementById("tbody_alert");o&&o.children.length===0&&(o.innerHTML='<tr><td style="text-align:center; color:grey; padding:20px;">새로운 알림이 없습니다.</td></tr>')}else{const n=await t.json().catch(()=>({message:"알림 삭제에 실패했습니다."}));throw new Error(n.message)}}catch(t){console.error("알림 삭제 실패:",t),alert(`오류: ${t.message}`)}}window.deleteNotification=h;async function L(e,t){try{await fetch(`/api/notifications/${t}/read`,{method:"POST"}),e&&(window.location.href=e)}catch(n){console.error("알림 읽음 처리 또는 이동 중 오류:",n),e&&(window.location.href=e)}}window.handleNotificationClick=L;async function k(e,t,n){const o=t?"accept":"reject";if(confirm(t?"친구 요청을 수락하시겠습니까?":"친구 요청을 거절하시겠습니까?"))try{const i=await fetch(`/api/friends/${o}/${e}`,{method:"PATCH"}),c=await i.json();if(i.ok)alert(`친구 요청을 ${t?"수락":"거절"}했습니다.`),h(n),t&&typeof f=="function"&&f();else throw new Error(c.message||"요청 처리에 실패했습니다.")}catch(i){console.error("친구 요청 처리 실패:",i),alert(`오류: ${i.message}`)}}window.handleFriendRequest=k;async function M(e,t,n){const o=t?"accept":"reject";if(confirm(t?"프로젝트 초대를 수락하시겠습니까?":"프로젝트 초대를 거절하시겠습니까?"))try{const i=await fetch(`/api/projects/${e}/invitations/${o}`,{method:"POST"}),c=await i.json();if(i.ok)alert(`프로젝트 초대를 ${t?"수락":"거절"}했습니다.`),h(n);else throw new Error(c.message||"요청 처리에 실패했습니다.")}catch(i){console.error("프로젝트 초대 처리 실패:",i),alert(`오류: ${i.message}`)}}window.handleProjectInvite=M;function x(){let e=document.querySelector(".modal2");if(!e){const n=document.createElement("div");n.innerHTML=`
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
        </dialog>`,document.body.appendChild(n),e=document.querySelector(".modal2"),setTimeout(()=>{v("friend1"),f()},0),document.body.style.overflow="hidden",e&&u(e);const o=document.getElementById("friendSearchBtn"),r=document.getElementById("friendSearchQuery");o&&r&&(o.onclick=()=>E(r.value.trim()),r.onkeypress=i=>{i.key==="Enter"&&E(r.value.trim())})}const t=document.querySelector("#friend");t&&t.addEventListener("click",()=>{let n=document.querySelector(".modal2");n||(x(),n=document.querySelector(".modal2")),n&&(n.showModal(),v("friend1"))}),e&&e.addEventListener("close",()=>{document.body.style.overflow="auto"})}function v(e){if(["friend1","friend2","friend3"].forEach(o=>{const r=document.getElementById(o);r&&(r.style.display=o===e?"block":"none")}),document.querySelectorAll(".button_friend").forEach((o,r)=>{"friend"+(r+1).toString()===e?o.classList.add("active"):o.classList.remove("active")}),e==="friend1")f();else if(e==="friend2"){const o=document.getElementById("userSearchResultTbody");o&&(o.innerHTML='<tr><td colspan="5" style="text-align:center; color:grey;">닉네임으로 사용자를 검색하세요.</td></tr>')}else e==="friend3"&&w()}window.Watchdiv=v;async function E(e){const t=document.getElementById("userSearchResultTbody");if(t){if(!e){t.innerHTML='<tr><td colspan="5" style="text-align:center; color:grey;">검색어를 입력해주세요.</td></tr>';return}t.innerHTML='<tr><td colspan="5" style="text-align:center; color:grey;">검색 중...</td></tr>';try{const n=await fetch(`/api/friends/search?query=${encodeURIComponent(e)}`);if(!n.ok){const r=await n.json().catch(()=>({message:"검색 중 오류 발생"}));throw new Error(r.message||`Error: ${n.status}`)}const o=await n.json();o.status===200&&Array.isArray(o.data)?S(o.data):t.innerHTML=`<tr><td colspan="5" style="text-align:center; color:red;">검색 결과를 가져오지 못했습니다: ${o.message}</td></tr>`}catch(n){console.error("Error searching users:",n),t.innerHTML=`<tr><td colspan="5" style="text-align:center; color:red;">검색 중 오류: ${n.message}</td></tr>`}}}function S(e){const t=document.getElementById("userSearchResultTbody");if(t.innerHTML="",e.length===0){t.innerHTML='<tr><td colspan="5" style="text-align:center; color:grey;">검색 결과가 없습니다.</td></tr>';return}e.forEach(n=>{const o=document.createElement("tr");o.innerHTML=`
                <td><img src="${n.profileImage||"/icon/user2.png"}" style="height: 30px; width: 30px; margin:5px; border-radius:50%; object-fit:cover;"/></td>
                <td class="lenCut_container"><span class="lenCut" title="${n.nickname}">${n.nickname}</span><div class="tooltip1"></div></td>
                <td><div class="lenCut_container"><span class="lenCutE" style="font-size: small; color: rgba(0, 0, 0, 0.5);" title="${n.email||""}">${n.email||"-"}</span><div class="tooltip1"></div></div></td>
                <td><span style="color: #3a6b5b;">#${n.identificationCode}</span></td>
                <td id="actionCell_${n.userIdx}"></td>
            `,t.appendChild(o),b(n.userIdx,n.friendStatus)}),m()}function b(e,t){const n=document.getElementById(`actionCell_${e}`);if(!n)return;n.innerHTML="";let o;switch(t){case"NONE":o=document.createElement("button"),o.className="button_select",o.style.width="80px",o.textContent="친구 신청",o.onclick=()=>I(e);break;case"PENDING_SENT":o=document.createElement("button"),o.className="button_sent",o.style.width="80px",o.textContent="요청 보냄",o.disabled=!0;break;case"PENDING_RECEIVED":n.innerHTML='<span style="font-size:small; color:orange;">요청 받음</span>';return;case"ACCEPTED":n.innerHTML='<span style="font-size:small; color:green;">친구</span>';return;case"SELF":n.innerHTML='<span style="font-size:small; color:grey;">본인</span>';return;default:n.innerHTML="-";return}o&&n.appendChild(o)}async function I(e){if(confirm("친구 요청을 보내시겠습니까?"))try{const t=await fetch("/api/friends/request",{method:"POST",headers:{"Content-Type":"application/json"},body:JSON.stringify({recipientUserId:e})}),n=await t.json();if(t.status===201)alert("친구 요청을 보냈습니다."),b(e,"PENDING_SENT");else throw new Error(n.message||"친구 요청 실패")}catch(t){console.error("Error sending friend request:",t),alert(`오류: ${t.message}`)}}async function f(){const e=document.getElementById("friendListTbody");if(e){e.innerHTML='<tr><td colspan="5" style="text-align:center; color:grey;">친구 목록 로딩 중...</td></tr>';try{const t=await fetch("/api/friends");if(!t.ok)throw new Error(`Error: ${t.status}`);const n=await t.json();if(e.innerHTML="",n.status===200&&Array.isArray(n.data)){if(n.data.length===0){e.innerHTML='<tr><td colspan="5" style="text-align:center; color:grey;">친구가 없습니다.</td></tr>';return}n.data.forEach(o=>{const r=document.createElement("tr");r.innerHTML=`
                        <td><img src="${o.profileImage||"/icon/user2.png"}" style="height: 30px; width: 30px; margin:5px; border-radius:50%; object-fit:cover;"/></td>
                        <td class="lenCut_container"><span class="lenCut" title="${o.nickname}">${o.nickname}</span><div class="tooltip1"></div></td>
                        <td><div class="lenCut_container"><span class="lenCutE" style="font-size: small; color: rgba(0, 0, 0, 0.5);" title="${o.email||""}">${o.email||"-"}</span><div class="tooltip1"></div></div></td>
                        <td><span style="color: #3a6b5b;">#${o.identificationCode}</span></td>
                        <td><button class="button_no" style="width:auto; padding:0 5px; height:25px; font-size:13px;" onclick="removeFriendHandler(${o.userIdx}, '${o.nickname}')">친구 삭제</button></td>
                    `,e.appendChild(r)}),m()}else e.innerHTML=`<tr><td colspan="5" style="text-align:center; color:red;">친구 목록 로드 실패: ${n.message}</td></tr>`}catch(t){console.error("Error loading friend list:",t),e.innerHTML='<tr><td colspan="5" style="text-align:center; color:red;">친구 목록 로드 오류.</td></tr>'}}}async function q(e,t){if(confirm(`'${t}'님을 친구 목록에서 삭제하시겠습니까?`))try{const n=await fetch(`/api/friends/${e}`,{method:"DELETE"});if(n.ok){alert("친구를 삭제했습니다."),f();const o=document.querySelector(`#actionCell_${e} button`);o&&o.textContent!=="친구 신청"&&b(e,"NONE")}else{const o=await n.json().catch(()=>({message:"친구 삭제 실패"}));throw new Error(o.message)}}catch(n){console.error("Error removing friend:",n),alert(`오류: ${n.message}`)}}window.removeFriendHandler=q;async function w(){const e=document.getElementById("receivedRequestsTbody");if(e){e.innerHTML='<tr><td colspan="5" style="text-align:center; color:grey;">받은 친구 요청 로딩 중...</td></tr>';try{const t=await fetch("/api/friends/requests/received");if(!t.ok)throw new Error(`Error: ${t.status}`);const n=await t.json();if(e.innerHTML="",n.status===200&&Array.isArray(n.data)){if(n.data.length===0){e.innerHTML='<tr><td colspan="5" style="text-align:center; color:grey;">받은 친구 요청이 없습니다.</td></tr>';return}n.data.forEach(o=>{const r=o.user,i=document.createElement("tr");i.innerHTML=`
                        <td><img src="${r.profileImage||"/icon/user2.png"}" style="height: 30px; width: 30px; margin:5px; border-radius:50%; object-fit:cover;"/></td>
                        <td class="lenCut_container"><span class="lenCut" title="${r.nickname}">${r.nickname}</span><div class="tooltip1"></div></td>
                        <td><div class="lenCut_container"><span class="lenCutE" style="font-size: small; color: rgba(0, 0, 0, 0.5);" title="${r.email||""}">${r.email||"-"}</span><div class="tooltip1"></div></div></td>
                        <td><span style="color: #3a6b5b;">#${r.identificationCode}</span></td>
                        <td>
                            <div style="display: flex; flex-direction: row; gap: 3px;">
                                <button class="button_yes" onclick="acceptFriendRequestHandler(${r.userIdx})">수락</button>
                                <button class="button_no" onclick="rejectFriendRequestHandler(${r.userIdx})">거절</button>
                            </div>
                        </td>
                    `,e.appendChild(i)}),m()}else e.innerHTML=`<tr><td colspan="5" style="text-align:center; color:red;">받은 요청 목록 로드 실패: ${n.message}</td></tr>`}catch(t){console.error("Error loading received requests:",t),e.innerHTML='<tr><td colspan="5" style="text-align:center; color:red;">받은 요청 목록 로드 오류.</td></tr>'}}}async function H(e){try{const t=await fetch(`/api/friends/accept/${e}`,{method:"PATCH"}),n=await t.json();if(t.ok)alert("친구 요청을 수락했습니다."),w(),f();else throw new Error(n.message||"친구 요청 수락 실패")}catch(t){console.error("Error accepting friend request:",t),alert(`오류: ${t.message}`)}}window.acceptFriendRequestHandler=H;async function _(e){try{const t=await fetch(`/api/friends/reject/${e}`,{method:"PATCH"}),n=await t.json();if(t.ok)alert("친구 요청을 거절했습니다."),w();else throw new Error(n.message||"친구 요청 거절 실패")}catch(t){console.error("Error rejecting friend request:",t),alert(`오류: ${t.message}`)}}window.rejectFriendRequestHandler=_;function m(){document.querySelectorAll(".lenCut_container").forEach(e=>{const t=e.querySelector(".lenCut"),n=e.querySelector(".lenCutE"),o=e.querySelector(".tooltip1"),r=(i,c)=>{if(i&&o){const l=i.dataset.originalText||i.textContent;i.dataset.originalText=l,l.length>c?i.textContent=l.slice(0,c)+"...":i.textContent=l,i.onmouseenter=function(){(i.offsetWidth<i.scrollWidth||i.textContent.endsWith("..."))&&(o.style.display="block",o.textContent=l)},i.onmouseleave=function(){o.style.display="none"}}};r(t,6),r(n,15)})}window.addEventListener("load",m);function N(){confirm("로그아웃 하겠습니까?")&&fetch("/api/auth/logout",{method:"POST"}).then(t=>{t.ok?window.location.href="/front/login.html?logout":alert("로그아웃에 실패했습니다. 다시 시도해주세요.")}).catch(t=>{console.error("Logout error:",t),alert("로그아웃 중 오류가 발생했습니다.")})}window.logout=N;function C(){let e=document.querySelector(".modal3");if(!e){const o=document.createElement("div");o.innerHTML=`
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
      </dialog>`,document.body.appendChild(o),e=document.querySelector(".modal3"),e?u(e):console.error("Failed to create or find .modal3 for mypage.")}const t=document.querySelector("#mypage"),n=()=>{const o=t.getBoundingClientRect(),r=e.getBoundingClientRect();e.style.position="fixed",e.style.left=`${o.right-r.width}px`,e.style.top=`${o.bottom}px`};return t.addEventListener("click",()=>{e.showModal(),n()}),window.addEventListener("resize",()=>{e.open&&n()}),e}async function P(){const e=document.getElementById("myPageDisplayId"),t=document.getElementById("myPageNickname"),n=document.getElementById("myPageTag"),o=document.getElementById("myPageBio"),r=document.getElementById("myPageProfileImage");if(!e||!t||!n||!o||!r){console.error("MyPage modal elements not found. Ensure createMyPageModalIfNeeded ran successfully.");return}try{const i=await fetch("/api/users/me");if(!i.ok){if(i.status===401){e.textContent="로그인 필요",t.textContent="",n.textContent="",o.textContent="로그인 후 이용해주세요.";return}throw new Error(`Failed to fetch user info: ${i.status}`)}const c=await i.json(),l=c.data||c;l?(l.loginId&&l.loginId.trim()!==""?e.textContent=l.loginId:e.textContent="소셜 로그인",t.textContent=l.nickname||"닉네임 정보 없음",n.textContent=l.identificationCode?`#${l.identificationCode}`:"",o.textContent=l.bio||"자기소개가 없습니다.",r.src=l.profileImage||"/icon/user2.png"):(console.error("User info data is not in expected format:",c),e.textContent="정보 형식 오류",t.textContent="",n.textContent="")}catch(i){console.error("Error fetching user info:",i),e.textContent="정보 로드 오류",t.textContent="",n.textContent="",o.textContent="정보를 불러오는 데 실패했습니다."}}async function R(){let e=C();e?(await P(),e.showModal()):console.error("Mypage modal (.modal3) could not be initialized for showing.")}function j(){window.location.href="/front/projectlist.html"}function B(){return d("div",{className:"root"},d("button",{className:"left1 drag1 clear",onClick:j},d("img",{src:"/icon/logo.png",className:"logo",style:{height:"50px"}})),d("button",{className:"right3 drag1 clear",onClick:R,id:"mypage"},d("i",{className:"fa-solid fa-user fa-2x",style:{fontSize:"25px"}})),d("button",{className:"right2 drag1 clear",onClick:()=>{let e=document.querySelector(".modal2");e||(x(),e=document.querySelector(".modal2")),e&&e.showModal()},id:"friend"},d("i",{className:"fa-solid fa-user-group fa-2x",style:{fontSize:"25px"}})),d("button",{className:"right1 drag1 clear",onClick:s,id:"alertr"},d("i",{className:"fa-solid fa-bell fa-2x",style:{fontSize:"25px"}})))}g(document.getElementById("root")).render(d(B));function T(){document.querySelector(".modal1")||s(),document.querySelector(".modal2")||x(),C()}window.addEventListener("load",T);let $=document.querySelector("#root");if($){let e=new MutationObserver(T),t={attributes:!0,childList:!0,characterData:!0};e.observe($,t)}document.querySelectorAll(".lenCut_container").forEach(e=>{const t=e.querySelector(".lenCut"),n=e.querySelector(".lenCutE"),o=e.querySelector(".tooltip1");if(t){const r=t.textContent;r.length>6&&(t.textContent=r.slice(0,6)+"...",o&&(t.addEventListener("mouseenter",function(){o.style.display="block",o.textContent=r}),t.addEventListener("mouseleave",function(){o.style.display="none"})))}if(n){const r=n.textContent;r.length>15&&(n.textContent=r.slice(0,15)+"...",o&&(n.addEventListener("mouseenter",function(){o.style.display="block",o.textContent=r}),n.addEventListener("mouseleave",function(){o.style.display="none"})))}}),document.querySelectorAll(".button_select").forEach(e=>{let t=!1;e.addEventListener("click",()=>{t=!t,e.classList.toggle("active"),t?e.textContent="취소":e.textContent="선택"}),e.addEventListener("mouseenter",()=>{t&&(e.textContent="취소")}),e.addEventListener("mouseleave",()=>{t&&(e.textContent="선택")})})})();
