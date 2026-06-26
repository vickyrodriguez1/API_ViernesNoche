import React, { useState } from 'react';
import { Link, useNavigate } from 'react-router-dom';
import { useDispatch } from 'react-redux';
import { showSnackbar } from '../../store/slices/uiSlice';
import styles from './LoginForm.module.css';

export default function LoginForm({ onLoginSuccess }) {
    const navigate = useNavigate();
    const dispatch = useDispatch();
    // 1. Estados para guardar los datos de entrada y errores
    const [email, setEmail] = useState('');
    const [password, setPassword] = useState('');
    const [errorMessage, setErrorMessage] = useState('');
    const [loading, setLoading] = useState(false);

    // 2. Función que maneja el envío del formulario al Backend
    const handleSubmit = async (e) => {
        e.preventDefault(); // Evita que la página se recargue
        setErrorMessage('');
        setLoading(true);

        const loginData = {
            email: email,
            password: password
        };

        try {
            // Conexión con tu backend de Spring Boot
            const response = await fetch('http://localhost:8080/api/auth/login', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                },
                body: JSON.stringify(loginData),
            });

            // Si el backend nos rebota, atrapamos el error personalizado
            if (!response.ok) {
                const errorData = await response.json();
                throw new Error(errorData.message || 'Contraseña o email incorrectos.');
            }

            // Si todo salió bien, recibimos el TokenResponseDTO con el JWT
            const data = await response.json();
            
            // Guardamos el token en localStorage para mandarlo en cada pedido (Bearer)
            // y para mantener la sesion aunque se recargue la pagina.
            localStorage.setItem('token', data.token);

            // El JWT tiene 3 partes (header.payload.signature). El payload es legible:
            // lo decodificamos con atob (Base64) para sacar el email, que viaja en "sub".
            const payload = JSON.parse(atob(data.token.split('.')[1]));
            const emailDelToken = payload.sub;

            // Consultamos el rol del usuario usando el email
            const userResponse = await fetch(`http://localhost:8080/api/usuarios/email/${emailDelToken}`, {
                headers: { 'Authorization': `Bearer ${data.token}` }
            });
            const userData = await userResponse.json();
            
            // Guardamos el rol en localStorage para usarlo en otros componentes
            localStorage.setItem('userRol', userData.rol);
            localStorage.setItem('userId', userData.id);

            if (onLoginSuccess) {
                onLoginSuccess(userData.rol);
            }
            // Cartel breve de exito (se cierra solo). Se dispara antes de navegar:
            // como el snackbar es global, sigue visible aunque cambiemos de ruta.
            dispatch(showSnackbar({ message: '¡Sesión iniciada con éxito!', type: 'success' }));
            navigate('/');

        } catch (error) {
            setErrorMessage(error.message);
        } finally {
            setLoading(false);
        }
    };

    return (
        <div className={styles.container}>
            <div className={styles.panel}>
                <h2 className={styles.title}>Iniciar Sesión</h2>
                <p className={styles.subtitle}>Ingresá tus datos para continuar</p>

                {/* Cartel de error mejorado */}
                {errorMessage && (
                    <div className={styles.errorAlert}>
                        ⚠️ {errorMessage}
                    </div>
                )}

                <form onSubmit={handleSubmit}>
                    {/* Campo de Email */}
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

                    {/* Campo de Contraseña */}
                    <div className={styles.formGroupPassword}>
                        <label className={styles.label}>Contraseña:</label>
                        <input 
                            type="password" 
                            className={styles.input}
                            value={password}
                            onChange={(e) => setPassword(e.target.value)}
                            placeholder="Ingresá tu contraseña"
                            required 
                        />
                    </div>

                    {/* Botón de Enviar */}
                    <button 
                        type="submit" 
                        disabled={loading}
                        className={styles.button}
                    >
                        {loading ? 'Validando...' : 'Ingresar'}
                    </button>
                </form>

                <p className={styles.switchText}>
                    ¿No tenés cuenta? <Link to="/register" className={styles.switchLink}>Registrate</Link>
                </p>
            </div>
        </div>
    );
}
