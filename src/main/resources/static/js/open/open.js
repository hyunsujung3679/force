$(function() {
    const openMoney = document.querySelector('#openMoney');

    openMoney.addEventListener('keyup', function(e) {
        let value = e.target.value;
        value = Number(value.replaceAll(',', ''));
        if(isNaN(value)) {         //NaN인지 판별
            openMoney.value = 0;
        }else {                   //NaN이 아닌 경우
            const formatValue = value.toLocaleString('ko-KR');
            openMoney.value = formatValue;
        }
    })
})