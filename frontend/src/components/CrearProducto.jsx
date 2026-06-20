import React, { useState } from 'react';
import styles from './CrearProducto.module.css';

export default function CrearProducto() {
    const [nombre, setNombre] = useState('');
    const [descripcion, setDescripcion] = useState('');
    const [precio, setPrecio] = useState('');
    const [stock, setStock] = useState('');
    const [imagenBase64, setImagenBase64] = useState(''); // 👈 Estado para guardar el string de la foto
    const [mensaje, setMensaje] = useState('');
    const [error, setError] = useState('');
    const [loading, setLoading] = useState(false);

    // Mágico convertidor de archivos a string Base64
    const handleImageChange = (e) => {
        const file = e.target.files[0];
        if (file) {
            const reader = new FileReader();
            reader.onloadend = () => {
                setImagenBase64(reader.result); // Guarda la conversión de la imagen
            };
            reader.readAsDataURL(file);
        }
    };

    const handleSubmit = async (e) => {
        e.preventDefault();
        setMensaje('');
        setError('');
        setLoading(true);

        const token = localStorage.getItem('token');
        const productoData = {
            nombre,
            descripcion,
            precio: parseFloat(precio),
            stock: parseInt(stock),
            imagen_base64: imagenBase64 // 👈 Sumamos la imagen mapeada con tu base de datos
        };

        try {
            const response = await fetch('http://localhost:8080/api/productos', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                    'Authorization': `Bearer ${token}`,
                },
                body: JSON.stringify(productoData),
            });

            if (!response.ok) {
                const errorData = await response.json();
                throw new Error(errorData.message || 'Error al crear el producto.');
            }

            const data = await response.json();
            setMensaje(`¡Producto "${data.nombre}" creado con éxito! (ID: ${data.id})`);
            
            // Limpiamos todos los campos del formulario tras el éxito
            setNombre('');
            setDescripcion('');
            setPrecio('');
            setStock('');
            setImagenBase64(''); 
        } catch (err) {
            setError(err.message);
        } finally {
            setLoading(false);
        }
    };

    return (
        <div className={styles.container}>
            <div className={styles.card}>
                <div className={styles.cardHeader}>
                    <h2 className={styles.title}>Crear Producto</h2>
                    <p className={styles.cardHeaderSub}>Panel de administración</p>
                </div>

                <div className={styles.body}>
                    {mensaje && (
                        <div className={`${styles.alert} ${styles.alertSuccess}`}>{mensaje}</div>
                    )}
                    {error && (
                        <div className={`${styles.alert} ${styles.alertError}`}>{error}</div>
                    )}

                    <form onSubmit={handleSubmit}>
                        <div className={styles.formGroup}>
                            <label className={styles.label}>Nombre</label>
                            <input
                                type="text"
                                className={styles.input}
                                value={nombre}
                                onChange={(e) => setNombre(e.target.value)}
                                placeholder="Nombre del producto"
                                required
                            />
                        </div>

                        <div className={styles.formGroup}>
                            <label className={styles.label}>Descripción</label>
                            <textarea
                                className={`${styles.input} ${styles.textarea}`}
                                value={descripcion}
                                onChange={(e) => setDescripcion(e.target.value)}
                                placeholder="Descripción del producto (mín. 10 caracteres)"
                                required
                                rows={3}
                            />
                        </div>

                        <div className={styles.row}>
                            <div className={styles.formGroup}>
                                <label className={styles.label}>Precio ($)</label>
                                <input
                                    type="number"
                                    className={styles.input}
                                    value={precio}
                                    onChange={(e) => setPrecio(e.target.value)}
                                    placeholder="0.00"
                                    required
                                    min="0.01"
                                    step="0.01"
                                />
                            </div>

                            <div className={styles.formGroup}>
                                <label className={styles.label}>Stock</label>
                                <input
                                    type="number"
                                    className={styles.input}
                                    value={stock}
                                    onChange={(e) => setStock(e.target.value)}
                                    placeholder="0"
                                    required
                                    min="0"
                                />
                            </div>
                        </div>

                        {/* 📸 👈 NUEVO: Campo selector de archivo para la foto */}
                        <div className={styles.formGroup}>
                            <label className={styles.label}>Imagen del Producto</label>
                            <input
                                type="file"
                                accept="image/*"
                                className={styles.input}
                                onChange={handleImageChange}
                            />
                            {/* Vista previa miniatura si ya seleccionaste una imagen */}
                            {imagenBase64 && (
                                <div style={{ marginTop: '14px', textAlign: 'center' }}>
                                    <p style={{ fontSize: '12px', color: 'var(--color-text-muted)', marginBottom: '6px' }}>
                                        Vista previa:
                                    </p>
                                    <img 
                                        src={imagenBase64} 
                                        alt="Preview" 
                                        style={{ maxHeight: '120px', borderRadius: '8px', objectFit: 'contain', border: '1px solid rgba(255,255,255,0.1)' }} 
                                    />
                                </div>
                            )}
                        </div>

                        <button
                            type="submit"
                            disabled={loading}
                            className={styles.button}
                        >
                            {loading ? 'Creando...' : 'Crear Producto'}
                        </button>
                    </form>
                </div>
            </div>
        </div>
    );
}