import React, { useState } from 'react';
import { Link, useNavigate } from 'react-router-dom';
import { useDispatch } from 'react-redux';
import { showSnackbar } from '../../store/slices/uiSlice';
import styles from './RegisterForm.module.css';

// Formulario de registro de usuario.
// Pega contra el endpoint publico POST /api/auth/register del backend de Spring Boot.
// Pide nombre, apellido, email y contraseña (lo que valida el UsuarioRegistroDTO).
export default function RegisterForm() {
    const navigate = useNavigate();
    const dispatch = useDispatch();

    // Un estado por cada campo controlado del formulario (useState).
    const [nombre, setNombre] = useState('');
    const [apellido, setApellido] = useState('');
    const [email, setEmail] = useState('');
    const [password, setPassword] = useState('');
    const [confirmPassword, setConfirmPassword] = useState('');
    const [errorMessage, setErrorMessage] = useState('');
    const [loading, setLoading] = useState(false);

    const handleSubmit = async (e) => {
        e.preventDefault();
        setErrorMessage('');

        // Validacion en el cliente: las dos contraseñas deben coincidir.
        // Cortamos ANTES de llamar a la API si no coinciden.
        if (password !== confirmPassword) {
            setErrorMessage('Las contraseñas no coinciden.');
            return;
        }

        setLoading(true);

        // Armamos el cuerpo con los datos que espera el backend.
        // rol "CLIENTE" por defecto: el alta de admins se hace por seed/Postman.
        const registroData = {
            nombre,
            apellido,
            email,
            password,
            rol: 'CLIENTE',
        };

        try {
            const response = await fetch('http://localhost:8080/api/auth/register', {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify(registroData),
            });

            if (!response.ok) {
                const errorData = await response.json();
                throw new Error(errorData.message || 'No se pudo registrar el usuario.');
            }

            // Registro OK: snackbar de exito (se cierra solo) y al login.
            dispatch(showSnackbar({ message: '¡Cuenta creada con éxito! Ya podés iniciar sesión.', type: 'success' }));
            navigate('/login');
        } catch (error) {
            setErrorMessage(error.message);
        } finally {
            setLoading(false);
        }
    };

    return (
        <div className={styles.container}>
            <div className={styles.panel}>
                <h2 className={styles.title}>Crear cuenta</h2>
                <p className={styles.subtitle}>Registrate para empezar a comprar</p>

                {errorMessage && (
                    <div className={styles.errorAlert}>⚠️ {errorMessage}</div>
                )}

                <form onSubmit={handleSubmit}>
                    <div className={styles.row}>
                        <div className={styles.formGroup}>
                            <label className={styles.label}>Nombre:</label>
                            <input
                                type="text"
                                className={styles.input}
                                value={nombre}
                                onChange={(e) => setNombre(e.target.value)}
                                placeholder="Tu nombre"
                                required
                            />
                        </div>

                        <div className={styles.formGroup}>
                            <label className={styles.label}>Apellido:</label>
                            <input
                                type="text"
                                className={styles.input}
                                value={apellido}
                                onChange={(e) => setApellido(e.target.value)}
                                placeholder="Tu apellido"
                                required
                            />
                        </div>
                    </div>

                    <div className={styles.formGroup}>
                        <label className={styles.label}>Email:</label>
                        <input
                            type="email"
                            className={styles.input}
                            value={email}
                            onChange={(e) => setEmail(e.target.value)}
                            placeholder="ejemplo@uade.edu.ar"
                            required
                        />
                    </div>

                    <div className={styles.formGroup}>
                        <label className={styles.label}>Contraseña:</label>
                        <input
                            type="password"
                            className={styles.input}
                            value={password}
                            onChange={(e) => setPassword(e.target.value)}
                            placeholder="Mínimo 6 caracteres"
                            minLength={6}
                            required
                        />
                    </div>

                    <div className={styles.formGroupLast}>
                        <label className={styles.label}>Repetir contraseña:</label>
                        <input
                            type="password"
                            className={styles.input}
                            value={confirmPassword}
                            onChange={(e) => setConfirmPassword(e.target.value)}
                            placeholder="Repetí tu contraseña"
                            minLength={6}
                            required
                        />
                    </div>

                    <button type="submit" disabled={loading} className={styles.button}>
                        {loading ? 'Creando...' : 'Registrarme'}
                    </button>
                </form>

                <p className={styles.switchText}>
                    ¿Ya tenés cuenta? <Link to="/login" className={styles.switchLink}>Iniciá sesión</Link>
                </p>
            </div>
        </div>
    );
}
