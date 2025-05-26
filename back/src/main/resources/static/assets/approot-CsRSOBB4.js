(function(){const c=document.createElement("link").relList;if(c&&c.supports&&c.supports("modulepreload"))return;for(const o of document.querySelectorAll('link[rel="modulepreload"]'))s(o);new MutationObserver(o=>{for(const l of o)if(l.type==="childList")for(const a of l.addedNodes)a.tagName==="LINK"&&a.rel==="modulepreload"&&s(a)}).observe(document,{childList:!0,subtree:!0});function v(o){const l={};return o.integrity&&(l.integrity=o.integrity),o.referrerPolicy&&(l.referrerPolicy=o.referrerPolicy),o.crossOrigin==="use-credentials"?l.credentials="include":o.crossOrigin==="anonymous"?l.credentials="omit":l.credentials="same-origin",l}function s(o){if(o.ep)return;o.ep=!0;const l=v(o);fetch(o.href,l)}})();(function(){const{createElement:r,Fragment:c}=React,{createRoot:v}=ReactDOM;function s(t){t.addEventListener("click",function(e){e.target===t&&t.close()})}window.closeModal=s;function o(){const t=document.querySelector(".modal1");if(!t){const d=document.createElement("div");d.innerHTML=`<dialog class="modal1" style="overflow-y:scroll; ">
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
          </dialog>`,document.body.appendChild(d);const i=document.querySelector(".modal1");s(i)}document.querySelector("#alert").addEventListener("click",()=>{t.showModal()})}function l(){if(!document.querySelector(".modal2")){const d=document.createElement("div");d.innerHTML=`
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
          </dialog>`,document.body.appendChild(d);const i=document.querySelector(".modal2");s(i)}const e=document.querySelector("#friend");e&&e.addEventListener("click",()=>{const d=document.querySelector(".modal2");d&&d.showModal()})}function a(t){["1","2","3"].forEach(i=>{const n=document.getElementById(i);n&&(n.style.display=i===t?"block":"none")}),document.querySelectorAll(".button_friend").forEach((i,n)=>{(n+1).toString()===t?i.classList.add("active"):i.classList.remove("active")})}window.addEventListener("load",()=>a("1")),window.Watchdiv=a;function p(){confirm("로그아웃 하겠습니까?")&&fetch("/api/auth/logout",{method:"POST"}).then(e=>{e.ok?window.location.href="/front/login.html?logout":alert("로그아웃에 실패했습니다. 다시 시도해주세요.")}).catch(e=>{console.error("Logout error:",e),alert("로그아웃 중 오류가 발생했습니다.")})}window.logout=p;function u(){if(!document.querySelector(".modal3")){const d=document.createElement("div");d.innerHTML=`
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
        </dialog>`,document.body.appendChild(d);const i=document.querySelector(".modal3");s(i)}const e=document.querySelector("#mypage");e&&e.addEventListener("click",()=>{const d=document.querySelector(".modal3");d&&d.showModal()})}function f(){window.location.href="/front/projectlist.html"}function g(){return r("div",{className:"root"},r("button",{className:"left1 drag1 clear",onClick:f},r("img",{src:"/icon/logo.png",className:"logo",style:{height:"50px"}})),r("button",{className:"right3 drag1 clear",onClick:u,id:"mypage"},r("i",{className:"fa-solid fa-user fa-2x",style:{fontSize:"25px"}})),r("button",{className:"right2 drag1 clear",onClick:l,id:"friend"},r("i",{className:"fa-solid fa-user-group fa-2x",style:{fontSize:"25px"}})),r("button",{className:"right1 drag1 clear",onClick:o,id:"alert"},r("i",{className:"fa-solid fa-bell fa-2x",style:{fontSize:"25px"}})))}v(document.getElementById("root")).render(r(g));let y=document.querySelector("#root");if(y){let t=new MutationObserver(()=>{o(),l(),u()});window.addEventListener("load",function(){o(),l(),u()});let e={attributes:!0,childList:!0,characterData:!0};t.observe(y,e)}document.querySelectorAll(".lenCut_container").forEach(t=>{const e=t.querySelector(".lenCut"),d=t.querySelector(".lenCutE"),i=t.querySelector(".tooltip1");if(e){const n=e.textContent;n.length>6&&(e.textContent=n.slice(0,6)+"...",i&&(e.addEventListener("mouseenter",function(){i.style.display="block",i.textContent=n}),e.addEventListener("mouseleave",function(){i.style.display="none"})))}if(d){const n=d.textContent;n.length>15&&(d.textContent=n.slice(0,15)+"...",i&&(d.addEventListener("mouseenter",function(){i.style.display="block",i.textContent=n}),d.addEventListener("mouseleave",function(){i.style.display="none"})))}}),document.querySelectorAll(".button_select").forEach(t=>{let e=!1;t.addEventListener("click",()=>{e=!e,t.classList.toggle("active"),e?t.textContent="취소":t.textContent="선택"}),t.addEventListener("mouseenter",()=>{e&&(t.textContent="취소")}),t.addEventListener("mouseleave",()=>{e&&(t.textContent="선택")})})})();
