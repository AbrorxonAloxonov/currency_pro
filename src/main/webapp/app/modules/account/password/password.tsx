import React, { useState, useEffect } from 'react';
import { ValidatedField, ValidatedForm } from 'react-jhipster';
import { Row, Col, Button } from 'reactstrap';
import { toast } from 'react-toastify';

import { useAppDispatch, useAppSelector } from 'app/config/store';
import { getSession } from 'app/shared/reducers/authentication';
import PasswordStrengthBar from 'app/shared/layout/password/password-strength-bar';
import { savePassword, reset } from './password.reducer';

export const PasswordPage = () => {
  const [password, setPassword] = useState('');
  const dispatch = useAppDispatch();

  useEffect(() => {
    dispatch(reset());
    dispatch(getSession());
    return () => {
      dispatch(reset());
    };
  }, []);

  const handleValidSubmit = ({ currentPassword, newPassword }) => {
    dispatch(savePassword({ currentPassword, newPassword }));
  };

  const updatePassword = event => setPassword(event.target.value);

  const account = useAppSelector(state => state.authentication.account);
  const successMessage = useAppSelector(state => state.password.successMessage);
  const errorMessage = useAppSelector(state => state.password.errorMessage);

  useEffect(() => {
    if (successMessage) {
      toast.success(successMessage);
    } else if (errorMessage) {
      toast.error(errorMessage);
    }
    dispatch(reset());
  }, [successMessage, errorMessage]);

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="password-title">
            [<strong>{account.login}</strong>] uchun parol
          </h2>
          <ValidatedForm id="password-form" onSubmit={handleValidSubmit}>
            <ValidatedField
              name="currentPassword"
              label="Parol"
              placeholder="Hozirgi parolingiz"
              type="password"
              validate={{
                required: { value: true, message: 'Parol kiritilishi zarur.' },
              }}
              data-cy="currentPassword"
            />
            <ValidatedField
              name="newPassword"
              label="Yangi parol"
              placeholder="Yangi parol"
              type="password"
              validate={{
                required: { value: true, message: 'Parol kiritilishi zarur.' },
                minLength: { value: 4, message: 'Parol uzunligi kamida 4 harfdan iborat bo`lsin' },
                maxLength: { value: 50, message: 'Parol 50 harfdan uzun bo`lmasin' },
              }}
              onChange={updatePassword}
              data-cy="newPassword"
            />
            <PasswordStrengthBar password={password} />
            <ValidatedField
              name="confirmPassword"
              label="Yangi parolni tasdig`i"
              placeholder="Yangi parolni tasdig`i"
              type="password"
              validate={{
                required: { value: true, message: 'Parol tasdig`i kiritilishi zarur.' },
                minLength: { value: 4, message: 'Parol tasdig`i uzunligi kamida 4 harfdan iborat bo`lsin' },
                maxLength: { value: 50, message: 'Parol tasdig`i 50 harfdan uzun bo`lmasin' },
                validate: v => v === password || 'Parol va uning tasdig`i mos kelmadi!',
              }}
              data-cy="confirmPassword"
            />
            <Button color="success" type="submit" data-cy="submit">
              Saqlash
            </Button>
          </ValidatedForm>
        </Col>
      </Row>
    </div>
  );
};

export default PasswordPage;
