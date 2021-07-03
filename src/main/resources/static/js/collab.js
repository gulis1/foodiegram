function getCollab() {

    $.get("/collab", data => {

        document.getElementById("restaurant-origin").innerText = data.origin;
        document.getElementById("restaurant-type").innerText = data.type;
        document.getElementById("restaurant-pais").innerText = data.pais;
        document.getElementById("restaurant-ciudad").innerText = data.ciudad;
        document.getElementById("restaurant-calle").innerText = data.calle;
        document.getElementById("restaurant-vip").innerText = data.vip;

        document.getElementById("tierSelectColumn").style.display = "table-cell";
        document.getElementById("updateButtonColumn").style.display = "table-cell";


    });

    $.get("/sponsor", data => {

        document.getElementById("restaurant-date").innerText = data.endtime;
        document.getElementById("restaurant-money").innerText = data.money + "€";
    });

}

getCollab();


function pay() {

    let tier = document.getElementById("tierSelect").value;

    if (tier !== "invalid") {

        $.post("/sponsor/payment/auth" + "?sponsorType=" + tier, data => {
            window.location.href = data;
        })


    }

}