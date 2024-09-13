function confirmDelete() {
    Swal.fire({
        title: '¿Estás seguro?',
        text: 'No podrás recuperar este elemento una vez eliminado.',
        icon: 'warning',
        showCancelButton: true,
        confirmButtonColor: '#3085d6',
        cancelButtonColor: '#d33',
        confirmButtonText: 'Sí, eliminar',
        cancelButtonText: 'Cancelar'
    }).then((result) => {
        if (result.isConfirmed) {
            // Si el usuario confirma, enviar el formulario
            document.getElementById('deleteForm').submit();
        }
    });
}


