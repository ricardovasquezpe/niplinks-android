document.onclick = function(e) {
    addListener(e.target);
};

function addListener(el){
    var max_num = 5;
    var selectors = [];
    var parent    = el.parentElement;
    var tmp       = getSelector(el);

    selectors.push(tmp);
    var i = 0;
    while( (tmp = getSelector(parent)) !== false && i < max_num){
        selectors.push(tmp);
        parent = parent.parentElement;
        i++;
    }

    Android.getClick(selectors.reverse().join(' '));
}

function getSelector(el){
   try{
       var tagName =  el.tagName.toLowerCase();
       var id = el.getAttribute('id') ? '#' + el.getAttribute('id') : '';
       var className = el.getAttribute('class') ? el.getAttribute('class').trim().split(/\s+/) : [];
       if(className.length > 3){
           className = [ className[0],  className[1],  className[2] ];
       }
       className = className.length ? '.' + className.join('.') : '';
       var attrName = el.getAttribute('name') ? '[name="' + el.getAttribute('name') + '"]' : '';
       return (tagName == 'html') ? false : (id && /\]/g.test(id) == false && /\d/g.test(id) == false ? id : tagName + ( attrName ? attrName : className )  );
   }catch(err){
       return false;
   }
};