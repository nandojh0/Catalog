let productsList = [];
let existingItems = [];

// Cargar productos desde el backend cuando la página carga
$(document).ready(function () {
    $.ajax({
        url: '/products/all', // Endpoint para obtener todos los productos
        type: 'POST',
        success: function (response) {
            if (response.code === '200') {
                productsList = response.data;
                populateProductSelect(); // Poblar los selects con los productos
                loadExistingItems(); // Llamar después de cargar los productos
            } else {
                alert("Error al cargar los productos: " + response.message);
            }
        },
        error: function () {
            alert("Error al comunicarse con el servidor.");
        }
    });
});

let productIndex = 0;

function addProduct() {
    let productList = $('#productList');
    let newProduct = `
        <div class="form-row align-items-center" data-index="${productIndex}">
            <div class="col">
                <label for="productSelect">Producto</label>
                <select name="items[${productIndex}].productId" class="form-control product-select">
                    <option value="">Seleccionar producto</option>
                </select>
            </div>
            <div class="col">
                <label for="quantity">Cantidad</label>
                <input type="number" name="items[${productIndex}].quantity" class="form-control" placeholder="Cantidad" min="1" required>
            </div>
            <div class="col-auto">
                <button type="button" class="btn btn-danger mt-4" onclick="removeProduct(this)">Eliminar</button>
            </div>
        </div>
        <hr>`;
    productList.append(newProduct);

    // Poblar el nuevo select
    let newSelect = productList.find('.product-select').last();
    productsList.forEach(product => {
        let option = $('<option>').val(product.id).text(product.name);
        newSelect.append(option);
    });

    // Incrementar el índice
    productIndex++;
}


function populateProductSelect() {
    let selects = $('.product-select');
    
    selects.each(function() {
        $(this).html('<option value="">Seleccionar producto</option>');
        productsList.forEach(product => {
            let option = $('<option>').val(product.id).text(product.name);
            $(this).append(option);
        });
    });
}

// Función para cargar los ítems existentes en el formulario
function loadExistingItems() {
    let itemsJson = $('#itemsJson').val();
    
    if (itemsJson) {
        existingItems = JSON.parse(itemsJson);
        setTimeout(function() {
            existingItems.forEach((item, index) => {
                if (index >= $('#productList .form-row').length) {
                    addProduct();
                }

                let row = $('#productList .form-row').eq(index);
                let select = row.find('.product-select');
                select.val(item.productId); // Establecer el valor del select
                row.find('input[name$="quantity"]').val(item.quantity); // Establecer la cantidad
            });
        }, 100); // Ajusta el tiempo si es necesario
    }
}

function removeProduct(element) {
    $(element).closest('.form-row').remove();
}
