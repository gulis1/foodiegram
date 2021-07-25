function loadPublication(data) {
    try {
        document.getElementById("post-modal-image").src = data.image;
        document.getElementById("post-modal-title").innerText = data.title;
        document.getElementById("post-modal-text").innerText = data.text;
    }

    catch (err) {

    }


    document.getElementById("post-modal-ratings").innerHTML = data.media + '<i class="bi bi-star"></i>' + '  ' + data.numerototalval + '<i class="bi bi-person"></i>';
    document.getElementById("post-modal-date").innerText = data.fecha;

    let xx = document.getElementById("post-modal-loc");
    if (data.pais) {

        xx.innerText = data.pais;

        if (data.ciudad)
            xx.innerText += `, ` + data.ciudad;
    }

    else
        xx.innerText = "";



    document.getElementById("page-mask").style.display = "flex";
}

function generateComment(comment) {
    let temp = document.getElementsByTagName("template")[0];
    let clon = temp.content.cloneNode(true);

    if (comment.pfp)
        clon.children[0].children[0].children[0].children[0].src = comment.pfp;

    clon.children[0].children[0].children[1].children[0].innerHTML = `<p class="username">${comment.user}: <span class="comment"> ${comment.text}</span></p>`;
    document.getElementById("post-modal-comments").appendChild(clon);

}

function loadComments(comments) {
    // Se vacian los comentarios que habia antes en el modal.
    $('#post-modal-comments').empty();
    comments.forEach(generateComment);}

function onPostClicked(e) {

    if (!window.matchMedia('(orientation: portrait)').matches)
        cargar(e);

    else
        window.location.href = "/pruebas/posts/" + e.dataset.postid;

}

function cargar(e) {
    resetRating();

    document.getElementById("post-modal-comment-input-field").value = "";
    document.getElementById("post-modal").dataset.postid = e.dataset.postid;


    // Se llama a /posts/postID para obtener la info de la publicacion
    $.get("/posts/" + e.dataset.postid, loadPublication);

    // Se llama a /posts/postId/comments para obtener la lista de los comentarios
    $.get("/posts/" + e.dataset.postid + "/comments", loadComments);

    $.get("/posts/" + e.dataset.postid + "/ratings/me", loadRatings);
}

function sendComment(event) {

    if (!event.keyCode || event.keyCode === 13) {

        let inputField = document.getElementById("post-modal-comment-input-field");
        let postID = document.getElementById("post-modal").dataset.postid

        if (inputField.value.length !== 0) {

            let newComment = {text: inputField.value};
            $.post("/posts/" + postID + "/comments", newComment, () =>  $.get("/posts/" + postID + "/comments", loadComments));
            inputField.value = "";

        }
    }
}

function resetRating() {

    for (let i = 1; i<=5; i++) {
        let xx = document.getElementById("star" + i);
        xx.classList.remove("bi-star-fill");
        xx.classList.add("bi-star")
    }
}


function drawRating(score) {

    resetRating();

    for (let  i = 1; i<=5; i++) {
        let xx = document.getElementById("star" + i);

        if (i <= score) {
            xx.classList.remove("bi-star");
            xx.classList.add("bi-star-fill");
        }
    }

    // let postid = document.getElementById("post-modal").dataset.postid;
    // $.get("/posts/" + postid, loadPublication);
}

function loadRatings(data) {

    drawRating(data.punt)
}

function setRating(target) {

    let rat = target.id;
    rat = parseInt(rat.substr(rat.length - 1));
    let postID = document.getElementById("post-modal").dataset.postid;

    data = {score: rat}
    $.post("/posts/" + postID + "/ratings", data, () =>  drawRating(rat) );

}

function buscar(t) {
    let xDD= document.getElementById("searchBar");
    window.location.href = "/pruebas/search?name=" + xDD.value;
}

function viewComments(but) {

    but.style.display = "none";
    document.getElementById("post-modal-image-container").style.display = "none";
    document.getElementById("post-modal-details").style.display = "none";
    document.getElementById("post-modal-comments").style.display = "block";
    document.getElementById("post-modal-comment-input").style.display = "flex";
    document.getElementById("hide-comments-button").style.display = "block";
    document.getElementsByClassName("foodiegram-bg2").item(0).style.bottom = "17rem";

}

function hideComments(but) {

    but.style.display = "none";
    document.getElementById("post-modal-image-container").style.display = "flex";
    document.getElementById("post-modal-details").style.display = "flex";
    document.getElementById("post-modal-comments").style.display = "none";
    document.getElementById("post-modal-comment-input").style.display = "none";
    document.getElementById("view-comments-button").style.display = "block";
    document.getElementsByClassName("foodiegram-bg2").item(0).style.bottom = "12rem";
}

document.addEventListener("click", e => {

    let pageMask = document.getElementById("page-mask");

    if (pageMask.style.display !== "none" && !e.target.closest(".modal-click-box"))
        document.getElementById("page-mask").style.display = "none";

})

document.addEventListener("keydown", e => {

    if (e.key === "Escape") {
        let pageMask = document.getElementById("page-mask");

        if (pageMask.style.display !== "none")
            document.getElementById("page-mask").style.display = "none";
    }
})

