(function() {
    const { createElement, Fragment } = React;
    const { createRoot } = ReactDOM;
    
    function createMyPageModalIfNeeded() {
      let modal = document.querySelector(".modal3");
      if (!modal) {
        const div = document.createElement("div");
        div.innerHTML = `
        <dialog class="modal3">
            <div class="middle3">            
                <div style="display: flex; justify-content: flex-end;" >
                    <div id="online">
                        <img src="/icon/online.png" style="height:20px; width:20px; margin:5px;/> 
                        <span class="font1">접속중</span>
                    </div>
                    <div id="offline">
                        <img src="/icon/offline.png" style="height:20px; width:20px; margin:5px;/> 
                        <span class="font1">마지막 접속시간: 00 00 00</span>
                    </div>
                    <form method="dialog">
                        <button style="background-color:transparent; border:none;" class="font1">X</button>
                    </form>
                </div>

                <div style="display: flex;">
                    <img id="friendPageProfileImage" src="/icon/user2.png" style="height:100px; width:100px; margin:10px; margin-right: 30px; margin-left: 20px border-radius: 50%;"/>
                    <div style="display: flex; flex-direction: column; justify-content: center; padding: 5px;">
                        <div style="display: flex; gap: 5px;">
                            <div class="black" id="friendPageNickname" style="font-size: larger;">닉네임 로딩 중</div>
                            <div style="color:green" id="friendPageTag">#0000</div>
                        </div>
                        <div id="friendPageDisplayId" style="color:gray;">id 로딩 중...</div>
                        <div class="box_introduce" id="friendPageBio">
                            자기소개 로딩 중...
                        </div>

                        <div style="display: flex; flex-direction: row; margin-top: 30px;">
                            <div id="NoFriend">
                                <button class="button_pro2">친구 추가</button>
                            </div>
                            <div id="YesFriend">
                                <button class="button_pro2">친구 삭제</button>
                            </div>
                        </div>  

                    </div>
                </div> 

            </div>  
        </dialog>`;
        document.body.appendChild(div);
        modal = document.querySelector('.modal3');
            if (modal) {
                closeModal(modal);
            } 
            else {
                    console.error("Failed to create or find .");
            }
        }
        const modalBtn = document.querySelector("#mypage");

        // const updateModalPosition = () => {
        //     const rect = modalBtn.getBoundingClientRect();
        //     const modalRect = modal.getBoundingClientRect();
        //     modal.style.position = "fixed";
        //     modal.style.left = `${rect.right - modalRect.width}px`;
        //     modal.style.top = `${rect.bottom}px`;
        // };

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
        
        function friendprofile() {
            return createElement(

                // 'div',
                // { className: 'root' },
                // createElement(
                // 'button',
                // { className: 'left1 drag1 clear', onClick: openProjectList},
                // createElement('img', { src: '/icon/logo.png', className: 'logo', style: {height: '50px'} })
                // ),
                // createElement(
                // 'button',
                // { className: 'right3 drag1 clear', onClick: handleMyPageClick, id: 'mypage'},
                // createElement('i', { className: 'fa-solid fa-user fa-2x', style: { fontSize: '25px'} })
                // ),
                // createElement(
                // 'button',
                // { className: 'right2 drag1 clear', 
                //     onClick: () => { 
                //     let friendModal = document.querySelector(".modal2"); 
                //     if (!friendModal) { openFriend(); friendModal = document.querySelector(".modal2");} 
                //     if (friendModal) friendModal.showModal();
                //     }, 
                //     id:'friend'
                // },
                // createElement('i', { className: 'fa-solid fa-user-group fa-2x', style: { fontSize: '25px'} })
                // ),
                // createElement(
                // 'button',
                // { className: 'right1 drag1 clear', onClick: openAlert , id:'alertr' 
                // },
                // createElement('i', { className: 'fa-solid fa-bell fa-2x', style: { fontSize: '25px'}})
                // )
            );
        }
        //createRoot(document.getElementById('')).render(createElement(friendprofile)); //div 아이디
    }

}) 
 
 
 
