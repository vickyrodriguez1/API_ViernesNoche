import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';

export default function LoginForm({ onLoginSuccess }) {
    const navigate = useNavigate();
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
            
            // Guardamos el token en el almacenamiento local del navegador
            localStorage.setItem('token', data.token);

            // Decodificamos el JWT para extraer el email (está en el campo "sub")
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

            alert('¡Inicio de sesión correcto!');

            if (onLoginSuccess) {
                onLoginSuccess(userData.rol);
            }
            navigate('/');

        } catch (error) {
            setErrorMessage(error.message);
        } finally {
            setLoading(false);
        }
    };

    return (
        <div style={{ maxWidth: '400px', margin: '50px auto', padding: '30px', border: '1px solid #ddd', borderRadius: '8px', boxShadow: '0px 4px 6px rgba(0,0,0,0.1)' }}>
            <h2 style={{ textAlign: 'center', marginBottom: '20px' }}>Iniciar Sesión</h2>
            
            {/* Si hay un error, se muestra el cartelito rojo */}
            {errorMessage && (
                <div style={{ color: 'red', backgroundColor: '#ffe6e6', padding: '10px', borderRadius: '4px', marginBottom: '15px', textAlign: 'center' }}>
                    {errorMessage}
                </div>
            )}

            <form onSubmit={handleSubmit}>
                {/* Campo de Email */}
                <div style={{ marginBottom: '15px' }}>
                    <label style={{ display: 'block', marginBottom: '5px', fontWeight: 'bold' }}>Email:</label>
                    <input 
                        type="email" 
                        value={email}
                        onChange={(e) => setEmail(e.target.value)}
                        placeholder="ejemplo@uade.edu.ar"
                        required 
                        style={{ width: '100%', padding: '10px', borderRadius: '4px', border: '1px solid #ccc', boxSizing: 'border-box' }}
                    />
                </div>

                {/* Campo de Contraseña */}
                <div style={{ marginBottom: '20px' }}>
                    <label style={{ display: 'block', marginBottom: '5px', fontWeight: 'bold' }}>Contraseña:</label>
                    <input 
                        type="password" 
                        value={password}
                        onChange={(e) => setPassword(e.target.value)}
                        placeholder="Ingresá tu contraseña"
                        required 
                        style={{ width: '100%', padding: '10px', borderRadius: '4px', border: '1px solid #ccc', boxSizing: 'border-box' }}
                    />
                </div>

                {/* Botón de Enviar */}
                <button 
                    type="submit" 
                    disabled={loading}
                    style={{ 
                        width: '100%', 
                        padding: '12px', 
                        background: loading ? '#ccc' : '#007bff', 
                        color: 'white', 
                        border: 'none', 
                        borderRadius: '4px', 
                        fontSize: '16px', 
                        cursor: loading ? 'not-allowed' : 'pointer' 
                    }}
                >
                    {loading ? 'Validando...' : 'Ingresar'}
                </button>
            </form>
        </div>
    );
}