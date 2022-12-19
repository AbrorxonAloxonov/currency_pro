import React, { useState, useEffect } from 'react';
import { Col, Row, Button } from 'reactstrap';
import { ValidatedField, ValidatedForm } from 'react-jhipster';
import { useSearchParams } from 'react-router-dom';
import { toast } from 'react-toastify';

import { handlePasswordResetFinish, reset } from '../password-reset.reducer';
import PasswordStrengthBar from 'app/shared/layout/password/password-strength-bar';
import { useAppDispatch, useAppSelector } from 'app/config/store';

export const PasswordResetFinishPage = () => {
  const dispatch = useAppDispatch();

  const [searchParams] = useSearchParams();
  const key = searchParams.get('key');

  const [password, setPassword] = useState('');

  useEffect(
    () => () => {
      dispatch(reset());
    },
    []
  );

  const handleValidSubmit = ({ newPassword }) => dispatch(handlePasswordResetFinish({ key, newPassword }));

  const updatePassword = event => setPassword(event.target.value);

  const getResetForm = () => {
    return (
      <ValidatedForm onSubmit={handleValidSubmit}>
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
          data-cy="resetPassword"
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
          data-cy="confirmResetPassword"
        />
        <Button color="success" type="submit" data-cy="submit">
          Yangi parolni tasdiqlayman
        </Button>
      </ValidatedForm>
    );
  };

  const successMessage = useAppSelector(state => state.passwordReset.successMessage);

  useEffect(() => {
    if (successMessage) {
      toast.success(successMessage);
    }
  }, [successMessage]);

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="4">
          <h1>Parolni tiklash</h1>
          <div>{key ? getResetForm() : null}</div>
        </Col>
      </Row>
    </div>
  );
};

export default PasswordResetFinishPage;
