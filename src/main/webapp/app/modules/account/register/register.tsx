import React, { useState, useEffect } from 'react';
import { ValidatedField, ValidatedForm, isEmail } from 'react-jhipster';
import { Row, Col, Alert, Button } from 'reactstrap';
import { toast } from 'react-toastify';

import PasswordStrengthBar from 'app/shared/layout/password/password-strength-bar';
import { useAppDispatch, useAppSelector } from 'app/config/store';
import { handleRegister, reset } from './register.reducer';

export const RegisterPage = () => {
  const [password, setPassword] = useState('');
  const dispatch = useAppDispatch();

  useEffect(
    () => () => {
      dispatch(reset());
    },
    []
  );

  const handleValidSubmit = ({ username, email, firstPassword }) => {
    dispatch(handleRegister({ login: username, email, password: firstPassword, langKey: 'en' }));
  };

  const updatePassword = event => setPassword(event.target.value);

  const successMessage = useAppSelector(state => state.register.successMessage);

  useEffect(() => {
    if (successMessage) {
      toast.success(successMessage);
    }
  }, [successMessage]);

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h1 id="register-title" data-cy="registerTitle">
            Ro&apos;yhatdan o&apos;tish
          </h1>
        </Col>
      </Row>
      <Row className="justify-content-center">
        <Col md="8">
          <ValidatedForm id="register-form" onSubmit={handleValidSubmit}>
            <ValidatedField
              name="username"
              label="Login"
              placeholder="Sizning loginingiz"
              validate={{
                required: { value: true, message: 'Login kiritilsin.' },
                pattern: {
                  value: /^[a-zA-Z0-9!$&*+=?^_`{|}~.-]+@[a-zA-Z0-9-]+(?:\\.[a-zA-Z0-9-]+)*$|^[_.@A-Za-z0-9-]+$/,
                  message: 'Your username is invalid.',
                },
                minLength: { value: 1, message: "Login kamida 1 ta harfdan iborat bo'lsin" },
                maxLength: { value: 50, message: "Login ko'pi bilan 50 ta harfdan iborat bo'lsin" },
              }}
              data-cy="username"
            />
            <ValidatedField
              name="email"
              label="Email"
              placeholder="Sizning email"
              type="email"
              validate={{
                required: { value: true, message: 'Email kiritilishi zarur.' },
                minLength: { value: 5, message: 'Email uzunligi kamida 5 harfdan iborat bo`lsin' },
                maxLength: { value: 254, message: 'Email 50 harfdan uzun bo`lmasin' },
                validate: v => isEmail(v) || 'Iltimos, to`g`ri Email kiriting.',
              }}
              data-cy="email"
            />
            <ValidatedField
              name="firstPassword"
              label="Yangi parol"
              placeholder="Yangi parol"
              type="password"
              onChange={updatePassword}
              validate={{
                required: { value: true, message: 'Parol kiritilishi zarur.' },
                minLength: { value: 4, message: 'Parol uzunligi kamida 4 harfdan iborat bo`lsin' },
                maxLength: { value: 50, message: 'Parol 50 harfdan uzun bo`lmasin' },
              }}
              data-cy="firstPassword"
            />
            <PasswordStrengthBar password={password} />
            <ValidatedField
              name="secondPassword"
              label="Yangi parolni tasdig`i"
              placeholder="Yangi parolni tasdig`i"
              type="password"
              validate={{
                required: { value: true, message: 'Parol tasdig`i kiritilishi zarur.' },
                minLength: { value: 4, message: 'Parol tasdig`i uzunligi kamida 4 harfdan iborat bo`lsin' },
                maxLength: { value: 50, message: 'Parol tasdig`i 50 harfdan uzun bo`lmasin' },
                validate: v => v === password || 'Parol va uning tasdig`i mos kelmadi!',
              }}
              data-cy="secondPassword"
            />
            <Button id="register-submit" color="primary" type="submit" data-cy="submit">
              Ro&apos;yhatdan o&apos;tish
            </Button>
          </ValidatedForm>
          <p>&nbsp;</p>
          <Alert color="warning">
            <span>Agar</span>
            <a className="alert-link">avtarizatsiyadan o`tishni</a>
            <span>
              xohlasangiz, mavjud foydalanuvchini tekshirib ko`ring:
              <br />- Ma`mur (login=&quot;admin&quot; va parol=&quot;admin&quot;) <br />- Oddiy foydalanuvchi (login=&quot;user&quot; va
              parol=&quot;user&quot;).
            </span>
          </Alert>
        </Col>
      </Row>
    </div>
  );
};

export default RegisterPage;
