document.addEventListener('DOMContentLoaded', function() {
    // Cargar las categorías con AJAX
    loadCategories();
});

function loadCategories() {
    fetch('/categories/all', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
            'Accept': 'application/json'
        }
    })
    .then(response => response.json())
    .then(data => {
        if (data.code === '200') {
            const categories = data.data;
            const select = document.getElementById('product.category');
            select.innerHTML = ''; // Limpiar las opciones existentes
            categories.forEach(category => {
                const option = document.createElement('option');
                option.value = category.id;
                option.textContent = category.name;
                select.appendChild(option);
            });
        } else {
            Swal.fire({
                icon: 'error',
                title: 'Error',
                text: data.message
            });
        }
    })
    .catch(error => {
        console.error('Error:', error);
        Swal.fire({
            icon: 'error',
            title: 'Error',
            text: 'Error al cargar las categorías.'
        });
    });
}